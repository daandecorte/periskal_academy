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
import { FormsModule } from '@angular/forms';
import { text } from '@fortawesome/fontawesome-svg-core';

@Component({
  selector: 'app-tips-and-tricks',
  imports: [FontAwesomeModule, TranslatePipe, FormsModule],
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

  @ViewChild('tipTopicRef') tipTopicRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('tipTextRef') tipTextRef!: ElementRef<HTMLTextAreaElement>;
  isLoaded: boolean = false;

  topics: ITopic[] = [];
  tips: ITip[] = [];
  tipsSorted: ITipSorted[] = [];
  filteredTipsSorted: ITipSorted[] = [];
  searchTerm: string = '';
  selectedTopic: string = 'all';
  sectionsOpenState: boolean[] = [];

  currentUser$: Observable<IUser | null>;
  currentUserRole: string | null = null;
  editorUser: boolean = false;

  modalOverlay!: HTMLDivElement;
  modalOverlayDelete!: HTMLDivElement;

  topicAdd: boolean = false;
  topicEdit: boolean = false;
  topicDelete: boolean = false;
  tipAdd: boolean = false;
  tipEdit: boolean = false;
  tipDelete: boolean = false;

  currentLanguage: keyof ITranslated = 'ENGLISH';
  selectedButtonLanguage: keyof ITranslated = 'ENGLISH';

  editId: number = 0;
  deleteId: number = 0;
  deleteTopic: boolean = false;

  newTopic: INewTopic = {
    title: {
      ENGLISH: '',
      GERMAN: '',
      DUTCH: '',
      FRENCH: '',
    },
  };

  newTip: INewTip = {
    topic_id: 0,
    text: {
      ENGLISH: '',
      GERMAN: '',
      DUTCH: '',
      FRENCH: '',
    },
  };

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

  async ngAfterViewInit() {
    this.loadTopicsAndTips();

    this.modalOverlay = document.getElementById(
      'modal-overlay'
    ) as HTMLDivElement;
    this.modalOverlayDelete = document.getElementById(
      'modal-overlay-delete'
    ) as HTMLDivElement;

    const buttonsLanguage =
      document.querySelectorAll<HTMLButtonElement>('.modal-button');

    buttonsLanguage.forEach((button) => {
      button.addEventListener('click', () => {
        buttonsLanguage.forEach((btn) => btn.classList.remove('selected'));

        button.classList.add('selected');

        this.changeModalLanguage();
      });
    });
  }

  async loadTopicsAndTips() {
    this.isLoaded = false;

    await this.getTopics();
    await this.getTips();
    this.sortTips();

    this.isLoaded = true;
  }

  async getTopics() {
    const topicsRaw = await fetch('/api/topics');
    this.topics = await topicsRaw.json();
  }

  async getTips() {
    const result = await fetch('/api/tips');
    this.tips = await result.json();
  }

  sortTips() {
    this.tipsSorted = [];

    for (let i = 0; i < this.topics.length; i++) {
      this.tipsSorted.push({
        topic_id: this.topics[i].id,
        title: this.topics[i].title,
        texts: [],
      });
      this.sectionsOpenState.push(false);
    }

    for (let i = 0; i < this.tips.length; i++) {
      let tip: ITip = this.tips[i];

      if (tip.topic == null) continue;

      for (let j = 0; j < this.tipsSorted.length; j++) {
        if (this.tipsSorted[j].topic_id == tip.topic.id) {
          this.tipsSorted[j].texts.push({
            id: tip.id,
            text: tip.text,
          });
        }
      }
    }

    this.filteredTipsSorted = this.tipsSorted;
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

  closeModalDelete() {
    this.modalOverlayDelete.classList.add('hidden');
  }

  changeModalLanguage() {
    const buttonLanguage =
      document.querySelector<HTMLButtonElement>('.selected');
    if (!buttonLanguage) return;

    this.selectedButtonLanguage = buttonLanguage.value as keyof ITranslated;

    if ((this.topicAdd || this.topicEdit) && this.tipTopicRef) {
      this.tipTopicRef.nativeElement.value =
        this.newTopic.title[this.selectedButtonLanguage] || '';
    }

    if ((this.tipAdd || this.tipEdit) && this.tipTextRef) {
      this.tipTextRef.nativeElement.value =
        this.newTip.text[this.selectedButtonLanguage] || '';
    }
  }

  async saveTopic() {
    if (this.topicAdd) {
      let result = await fetch(`/api/topics`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          title: {
            ENGLISH: this.newTopic.title.ENGLISH,
            GERMAN: this.newTopic.title.GERMAN,
            DUTCH: this.newTopic.title.DUTCH,
            FRENCH: this.newTopic.title.FRENCH,
          },
        }),
      });
      const data = await result;
    } else if (this.topicEdit) {
      let result = await fetch(`/api/topics/${this.editId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          title: {
            ENGLISH: this.newTopic.title.ENGLISH,
            GERMAN: this.newTopic.title.GERMAN,
            DUTCH: this.newTopic.title.DUTCH,
            FRENCH: this.newTopic.title.FRENCH,
          },
        }),
      });
      const data = await result;
    }
    this.closeModal();
    this.loadTopicsAndTips();
  }

  async saveTip() {
    if (this.tipAdd) {
      let result = await fetch(`/api/tips`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          topic_id: this.newTip.topic_id,
          text: {
            ENGLISH: this.newTip.text.ENGLISH,
            GERMAN: this.newTip.text.GERMAN,
            DUTCH: this.newTip.text.DUTCH,
            FRENCH: this.newTip.text.FRENCH,
          },
        }),
      });
      const data = await result;
    } else if (this.tipEdit) {
      let result = await fetch(`/api/tips/${this.editId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({
          topic_id: this.newTip.topic_id,
          text: {
            ENGLISH: this.newTip.text.ENGLISH,
            GERMAN: this.newTip.text.GERMAN,
            DUTCH: this.newTip.text.DUTCH,
            FRENCH: this.newTip.text.FRENCH,
          },
        }),
      });
    }
    this.closeModal();
    this.loadTopicsAndTips();
  }

  addTopicShow() {
    this.modalOverlay.classList.remove('hidden');
    this.topicAdd = true;
    this.topicEdit = false;
    this.tipAdd = false;
    this.tipEdit = false;
  }

  addTipShow(topic_id: number) {
    this.modalOverlay.classList.remove('hidden');
    this.topicAdd = false;
    this.topicEdit = false;
    this.tipAdd = true;
    this.tipEdit = false;

    this.newTip.topic_id = topic_id;
  }

  async editTopicShow(id: number) {
    this.topicAdd = false;
    this.topicEdit = true;
    this.tipAdd = false;
    this.tipEdit = false;

    this.editId = id;
    this.modalOverlay.classList.remove('hidden');

    const result = await fetch(`/api/topics/${this.editId}`);
    const data: ITopic = await result.json();

    this.newTopic = {
      title: data.title,
    };

    setTimeout(() => {
      if (this.tipTopicRef) {
        this.tipTextRef.nativeElement.value =
          data.title[this.selectedButtonLanguage];
      }
    }, 0);
  }

  async editTipShow(id: number) {
    this.topicAdd = false;
    this.topicEdit = false;
    this.tipAdd = false;
    this.tipEdit = true;

    this.editId = id;
    this.modalOverlay.classList.remove('hidden');

    const result = await fetch(`/api/tips/${this.editId}`);
    const data: ITip = await result.json();

    this.newTip = {
      topic_id: data.topic.id,
      text: data.text,
    };

    setTimeout(() => {
      if (this.tipTextRef) {
        this.tipTextRef.nativeElement.value =
          data.text[this.selectedButtonLanguage];
      }
    }, 0);
  }

  topicChange() {
    const buttonLanguage =
      document.querySelector<HTMLButtonElement>('.selected');

    if (buttonLanguage) {
      const language = buttonLanguage.value as keyof ITranslated;
      this.newTopic.title[language] = this.tipTopicRef.nativeElement.value;
    }
  }

  tipChange() {
    const buttonLanguage =
      document.querySelector<HTMLButtonElement>('.selected');

    if (buttonLanguage) {
      const language = buttonLanguage.value as keyof ITranslated;
      this.newTip.text[language] = this.tipTextRef.nativeElement.value;
    }
  }

  deleteTopicShow(id: number) {
    this.modalOverlayDelete.classList.remove('hidden');

    this.topicDelete = true;
    this.tipDelete = false;

    this.setDelete(id, true);
  }

  deleteTipShow(id: number) {
    this.modalOverlayDelete.classList.remove('hidden');

    this.topicDelete = false;
    this.tipDelete = true;

    this.setDelete(id, false);
  }

  setDelete(id: number, topic: boolean) {
    this.deleteId = id;
    this.deleteTopic = topic;
  }

  async delete() {
    if (this.deleteTopic) {
      await this.deleteTips();

      await fetch(`/api/topics/${this.deleteId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });
    } else {
      await fetch(`/api/tips/${this.deleteId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });
    }

    this.closeModalDelete();
    this.loadTopicsAndTips();
  }

  async deleteTips() {
    for (let i = 0; i < this.tips.length; i++) {
      if (this.tips[i].topic.id == this.deleteId) {
        await fetch(`/api/tips/${this.tips[i].id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
        });
      }
    }
  }

  toggleSection(event: Event, index: number): void {
    if (
      (this.tipAdd || this.topicEdit || this.topicDelete) &&
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

  filterTopics() {
    this.filteredTipsSorted = this.tipsSorted
      .map((topic) => {
        const title = topic.title[this.currentLanguage]?.toLowerCase();
        const matchesSelectedTopic =
          this.selectedTopic && this.selectedTopic !== 'all'
            ? title === this.selectedTopic.toLowerCase()
            : true;

        const filteredTexts = topic.texts.filter((t) =>
          this.searchTerm
            ? t.text[this.currentLanguage]
                ?.toLowerCase()
                .includes(this.searchTerm.toLowerCase())
            : true
        );

        return matchesSelectedTopic
          ? {
              ...topic,
              texts: filteredTexts,
            }
          : null;
      })
      .filter((topic) => topic !== null);
  }
}

//#region Interfaces
interface ITopic {
  id: number;
  title: ITranslated;
}

interface ITip {
  id: number;
  topic: ITopic;
  text: ITranslated;
}

interface ITranslated {
  ENGLISH: string;
  FRENCH: string;
  DUTCH: string;
  GERMAN: string;
}

interface ITipSorted {
  topic_id: number;
  title: ITranslated;
  texts: {
    id: number;
    text: ITranslated;
  }[];
}

interface INewTopic {
  title: ITranslated;
}

interface INewTip {
  topic_id: number;
  text: ITranslated;
}

//#endregion
