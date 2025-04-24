import { Component, OnInit, ViewChild, ElementRef, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TrainingService } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-module-video-view',
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule],
  templateUrl: './module-video-view.component.html',
  styleUrl: './module-video-view.component.css'
})
export class ModuleVideoViewComponent implements OnInit, OnDestroy {
  @ViewChild('videoPlayer') videoPlayer!: ElementRef<HTMLVideoElement>;
  
  trainingId: number = 0;
  moduleId: number = 0;
  currentStep: number = 1; // Video is always step 1
  totalSteps: number = 1; // Default to 1 (video + 0 question)
  questionCount: number = 0;

  moduleTitle: string = '';
  moduleDescription: string = '';
  videoUrl: string = '';
  currentLanguage: string = 'EN'; // Default language
  
  // Video progress tracking
  videoProgressKey: string = '';
  videoCurrentTime: number = 0;
  videoCompleted: boolean = false;
  videoProgressThreshold: number = 0.9; // 90% completion is considered complete
  
  // Subscription management
  private subscriptions: Subscription[] = [];
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    const languageSub = this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = this.mapLanguageCode(language);
      // Reload module data when language changes to get the right video
      if (this.trainingId && this.moduleId) {
        this.loadModuleData();
      }
    });
    this.subscriptions.push(languageSub);

    const routeSub = this.route.params.subscribe(params => {
      this.trainingId = +params['id'];
      this.moduleId = +params['sectionId'];
      this.videoProgressKey = `video_progress_${this.trainingId}_${this.moduleId}`;
      this.loadModuleData();
    });
    this.subscriptions.push(routeSub);
  }

  ngAfterViewInit(): void {
    // Restore video progress after the view is initialized
    this.restoreVideoProgress();
  }

  ngOnDestroy(): void {
    // Save progress when component is destroyed
    this.saveVideoProgress();
    
    // Clean up subscriptions
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // Listen for browser refresh or tab close
  @HostListener('window:beforeunload')
  saveBeforeUnload(): void {
    this.saveVideoProgress();
  }

  mapLanguageCode(language: any): string {
    const languageMappings: { [key: string]: string } = {
      'ENGLISH': 'EN',
      'FRENCH': 'FR',
      'DUTCH': 'NL',
      'GERMAN': 'DE'
    };

    if (['EN', 'FR', 'NL', 'DE'].includes(language)) {
      return language;
    }

    return languageMappings[language] || 'EN';
  }

  loadModuleData(): void {
    const trainingSub = this.trainingService.getTrainingById(this.trainingId).subscribe(
      training => {
        if (training && training.modules) {
          // Find the specific module in the training modules
          const module = training.modules.find((m: any) => m.id === this.moduleId);
          
          if (module) {
            this.setModuleData(module);
            // Restore video progress after loading module data
            setTimeout(() => this.restoreVideoProgress(), 300);
          } else {
            console.error(`Module with ID ${this.moduleId} not found in training ${this.trainingId}`);
            this.goBackToOverview();
          }
        } else {
          console.error(`Training with ID ${this.trainingId} not found or has no modules`);
          this.goBackToOverview();
        }
      },
      error => {
        console.error('Error loading module data:', error);
        // Try to use hardcoded data for demo purposes
        this.moduleTitle = "Demo Module";
        this.moduleDescription = "This is a demo module description.";
        this.videoUrl = "https://academyws.periskal.com/Downloads/groep7/TEST.mp4";
        this.questionCount = 2; // Default to 2 questions
        this.totalSteps = this.questionCount + 1; // Video + 2 questions = 3 steps total
      }
    );
    this.subscriptions.push(trainingSub);
  }

  setModuleData(module: any): void {
    this.moduleTitle = this.getLocalizedContent(module.title);
    this.moduleDescription = this.getLocalizedContent(module.description);
    
    // Set video URL based on current language
    if (module.videoReference) {
      this.videoUrl = this.getLocalizedContent(module.videoReference);
    } else {
      //This is for testing and should be removed later
      this.videoUrl = "https://academyws.periskal.com/Downloads/groep7/TEST.mp4";
    }
    
    // Set question count for total steps
    if (module.questions && module.questions.length > 0) {
      this.questionCount = module.questions.length;
      this.totalSteps = this.questionCount + 1; // Video + questions count
    } else {
      // If no questions are available, set to zero
      this.questionCount = 0; // Default to 0 question
      this.totalSteps = 1;
    }
  }

  // Calculate progress percentage for the progress bar
  getProgressPercentage(): number {
    if (this.totalSteps === 0) return 0;
    return ((this.currentStep) / this.totalSteps * 100);
  }

  // Get the step indicator text in the same format as the question component
  getStepIndicatorText(): string {
    return `${this.currentStep} ${this.getLocalizedContent({ 'EN': 'of', 'FR': 'de', 'NL': 'van', 'DE': 'von' })} ${this.totalSteps}`;
  }

  getLocalizedContent(contentMap: any): string {
    if (!contentMap) return '';
    if (typeof contentMap === 'string') return contentMap;
    
    // Try current language first
    if (contentMap[this.currentLanguage]) {
      return contentMap[this.currentLanguage];
    }
    
    // Fallback to English
    if (contentMap['EN']) {
      return contentMap['EN'];
    }
    
    // If none of the above, take the first available
    const values = Object.values(contentMap);
    return values.length > 0 ? values[0] as string : '';
  }

  goBackToOverview(): void {
    this.router.navigate(['/trainings', this.trainingId]);
  }

  continueToQuestions(): void {
    this.router.navigate(['/trainings', this.trainingId, 'module', this.moduleId, 'questions', 0]);
  }
  
  // Video functionality
  
  updateVideoProgress(event: Event): void {
    if (!this.videoPlayer) return;
    
    const video = this.videoPlayer.nativeElement;
    this.videoCurrentTime = video.currentTime;
    
    // Check if video has been watched to the completion threshold
    if (!this.videoCompleted && video.duration > 0) {
      const progressPercentage = video.currentTime / video.duration;
      if (progressPercentage >= this.videoProgressThreshold) {
        this.videoCompleted = true;
        // API call to mark the video as completed?
        console.log('Video completed!');
      }
    }
  }
  
  saveVideoProgress(): void {
    if (!this.videoPlayer || !this.videoProgressKey) return;
    
    const video = this.videoPlayer.nativeElement;
    const progressData = {
      currentTime: video.currentTime,
      completed: this.videoCompleted,
      timestamp: new Date().getTime(),
      duration: video.duration
    };
    
    // Save to localStorage
    localStorage.setItem(this.videoProgressKey, JSON.stringify(progressData));
    console.log('Video progress saved:', progressData);
  }
  
  restoreVideoProgress(): void {
    if (!this.videoPlayer || !this.videoPlayer.nativeElement) return;
    
    const savedProgress = localStorage.getItem(this.videoProgressKey);
    if (savedProgress) {
      try {
        const progressData = JSON.parse(savedProgress);
        
        // Only restore if saved within the last 30 days (in milliseconds)
        const thirtyDaysMs = 30 * 24 * 60 * 60 * 1000;
        const isRecent = (new Date().getTime() - progressData.timestamp) < thirtyDaysMs;
        
        if (isRecent && progressData.currentTime > 0) {
          this.videoPlayer.nativeElement.currentTime = progressData.currentTime;
          this.videoCompleted = progressData.completed || false;
          console.log('Video progress restored:', progressData);
        }
      } catch (e) {
        console.error('Error restoring video progress:', e);
      }
    }
  }
}