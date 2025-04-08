import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import {
  faGlobe,
  faInfoCircle,
  faAngleDown,
  faAngleUp,
  faSearch,
  faAdd,
  faPencil,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../services/auth.service';
import { LanguageService } from '../services/language.service';
import { TranslatePipe } from '@ngx-translate/core';
import { IUser } from '../types/user-info';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tips-and-tricks',
  imports: [FontAwesomeModule, TranslatePipe],
  templateUrl: './tips-and-tricks.component.html',
  styleUrl: './tips-and-tricks.component.css',
})
export class TipsAndTricksComponent implements AfterViewInit {
  faGlobe = faGlobe;
  faInfo = faInfoCircle;
  faAngleUp = faAngleUp;
  faAngleDown = faAngleDown;
  faSearch = faSearch;
  faAdd = faAdd;
  faPencil = faPencil;
  faTrash = faTrash;

  @ViewChild('tipContentRef') tipContentRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('tipTitleRef') tipTitleRef!: ElementRef<HTMLTextAreaElement>;
  isLoaded: boolean = false;

  tips: ITip[] = [];
  tipsSorted: ITipSorted[] = [];
  sectionsOpenState: boolean[] = [];

  currentUser$: Observable<IUser | null>;
  currentUserRole: string | null = null;
  editorUser: boolean = false;

  modalOverlay!: HTMLDivElement;
  modalOverlayDelete!: HTMLDivElement;

  titleAdd: boolean = false;
  titleEdit: boolean = false;
  titleDelete: boolean = false;
  tipAdd: boolean = false;
  tipEdit: boolean = false;
  tipDelete: boolean = false;

  currentLanguage: keyof ITranslated = 'ENGLISH';

  constructor(
    private authService: AuthService,
    private languageService: LanguageService
  ) {
    this.currentUser$ = this.authService.currentUser$;
    this.authService.currentUser$.subscribe((user) => {
      this.currentUserRole = user ? user.Role : null;
    });

    if (
      this.currentUserRole?.toLowerCase() == 'support' ||
      this.currentUserRole?.toLowerCase() == 'administrator'
    ) {
      this.editorUser = true;
    }

    this.languageService.currentLanguage$.subscribe((language) => {
      this.currentLanguage = language as keyof ITranslated;
    });
  }

  grouped: Map<string, { title: ITranslated; texts: ITranslatedId[] }> =
    new Map();

  async getTips() {
    const result = await fetch('api/tips');
    let tips: ITip[] = await result.json();
    return tips;
  }

  transformTips(tips: ITip[]): ITipSorted[] {
    for (const tip of tips) {
      const key = tip.title.ENGLISH;

      const translatedId: ITranslatedId = {
        ID: tip.id,
        ENGLISH: tip.text.ENGLISH,
        FRENCH: tip.text.FRENCH,
        DUTCH: tip.text.DUTCH,
        GERMAN: tip.text.GERMAN,
      };

      if (!this.grouped.has(key)) {
        this.grouped.set(key, {
          title: tip.title,
          texts: [translatedId],
        });
        this.sectionsOpenState.push(false);
      } else {
        this.grouped.get(key)!.texts.push(translatedId);
      }
    }

    return Array.from(this.grouped.values());
  }

  async ngAfterViewInit() {
    const tips = await this.getTips();
    this.tipsSorted = this.transformTips(tips);
    this.isLoaded = true;

    this.modalOverlay = document.getElementById(
      'modal-overlay'
    ) as HTMLDivElement;
    this.modalOverlayDelete = document.getElementById(
      'modal-overlay-delete'
    ) as HTMLDivElement;

    const buttons =
      document.querySelectorAll<HTMLButtonElement>('.modal-button');

    buttons.forEach((button) => {
      button.addEventListener('click', () => {
        buttons.forEach((btn) => btn.classList.remove('selected'));

        button.classList.add('selected');
      });
    });
  }

  closeModal() {
    const tipTitle = document.getElementById('tip-title') as HTMLInputElement;
    const tipText = document.getElementById(
      'tip-content'
    ) as HTMLTextAreaElement;

    if (tipText) tipText.value = '';
    if (tipTitle) tipTitle.value = '';

    this.modalOverlay.classList.add('hidden');
  }

  saveTopic() {}

  saveTip() {}

  addTopicShow() {
    this.modalOverlay.classList.remove('hidden');
    this.titleAdd = true;
    this.titleEdit = false;
    this.tipAdd = false;
    this.tipEdit = false;
  }

  editTitleShow(title: string | undefined) {
    this.titleAdd = false;
    this.titleEdit = true;
    this.tipAdd = false;
    this.tipEdit = false;

    this.modalOverlay.classList.remove('hidden');

    setTimeout(() => {
      if (title && this.tipTitleRef) {
        this.tipTitleRef.nativeElement.value = title;
      }
    }, 0);
  }

  addTipShow() {
    this.modalOverlay.classList.remove('hidden');
    this.titleAdd = false;
    this.titleEdit = false;
    this.tipAdd = true;
    this.tipEdit = false;
  }

  editTipShow(tip: string | undefined) {
    this.titleAdd = false;
    this.titleEdit = false;
    this.tipAdd = false;
    this.tipEdit = true;

    this.modalOverlay.classList.remove('hidden');
    setTimeout(() => {
      if (tip && this.tipContentRef) {
        this.tipContentRef.nativeElement.value = tip;
      }
    }, 0);
  }

  closeModalDelete() {
    this.modalOverlayDelete.classList.add('hidden');
  }

  deleteTopicShow() {
    this.modalOverlayDelete.classList.remove('hidden');

    this.titleDelete = true;
    this.tipDelete = false;
  }

  deleteTipShow() {
    this.modalOverlayDelete.classList.remove('hidden');

    this.titleDelete = false;
    this.tipDelete = true;
  }

  toggleSection(event: Event, index: number): void {
    if (
      (this.tipAdd || this.titleEdit || this.titleDelete) &&
      (!this.modalOverlay.classList.contains('hidden') ||
        !this.modalOverlayDelete.classList.contains('hidden'))
    )
      return;

    const header = event.currentTarget as HTMLElement;
    header.classList.toggle('collapsed');

    const content = header.nextElementSibling as HTMLElement;
    if (content) {
      content.classList.toggle('hidden');
    }

    this.sectionsOpenState[index] = !this.sectionsOpenState[index];
  }
}

interface ITip {
  id: number;
  title: ITranslated;
  text: ITranslated;
}

interface ITranslated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}

interface ITipSorted {
  title: ITranslated;
  texts: ITranslatedId[];
}

interface ITranslatedId {
  ID: number;
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}
