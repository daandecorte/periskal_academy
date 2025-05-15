import { Component, OnInit, ViewChild, ElementRef, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Module, TrainingService } from '../services/training.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { LanguageService } from '../services/language.service';
import { Subscription } from 'rxjs';
import { ContentType } from '../services/training.service';

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
  
  public module: Module = {
    id: 0,
    title: {},
    description: {},
    questions: [],
    content: []
  };
  // Subscription management
  private subscriptions: Subscription[] = [];
  
  // Font Awesome icons
  faArrowLeft = faArrowLeft;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    public languageService: LanguageService
  ) {}

  ngOnInit(): void {
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

  async loadModuleData() {

    let response = await fetch(`/api/modules/${this.moduleId}`);
    if(response.status!=200) {
      let errorText = await response.text
      console.log("error fetching module" + errorText);
    }
    else {
      let json = await response.json();
      this.module = await json;
      console.log(this.module);
    }
  }
  
  getProgressPercentage(): number {
    if (this.totalSteps === 0) return 0;
    return ((this.currentStep) / this.totalSteps * 100);
  }

  getStepIndicatorText(): string {
    return `${this.currentStep} of ${this.totalSteps}`;
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