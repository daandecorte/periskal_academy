import { Component, AfterViewInit } from '@angular/core';
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
import { IUser } from '../types/user-info';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tips-and-tricks',
  imports: [FontAwesomeModule],
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

  sectionsOpenState: boolean[] = [false, false];

  currentUser$: Observable<IUser | null>;
  currentUserRole: string | null = null;
  editorUser: boolean = false;

  modalOverlay!: HTMLDivElement;
  modalOverlayDelete!: HTMLDivElement;

  saveModalBtn!: HTMLButtonElement;
  closeModalBtn!: HTMLButtonElement;

  titleAdd: boolean = false;
  titleEdit: boolean = false;
  titleDelete: boolean = false;
  tipAdd: boolean = false;
  tipEdit: boolean = false;
  tipDelete: boolean = false;

  constructor(private authService: AuthService) {
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
  }

  ngAfterViewInit() {
    this.modalOverlay = document.getElementById(
      'modal-overlay'
    ) as HTMLDivElement;
    this.modalOverlayDelete = document.getElementById(
      'modal-overlay-delete'
    ) as HTMLDivElement;
    this.closeModalBtn = document.getElementById(
      'close-modal'
    ) as HTMLButtonElement;

    let editTitleIcons = document.getElementsByClassName(
      'edit-title'
    ) as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < editTitleIcons.length; i++) {
      editTitleIcons[i].addEventListener('click', () => {
        this.editTitleShow();
      });
    }

    let deleteTopicIcons = document.getElementsByClassName(
      'delete-topic'
    ) as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < deleteTopicIcons.length; i++) {
      deleteTopicIcons[i].addEventListener('click', () => {
        this.deleteTopicShow();
      });
    }

    let addTipIcons = document.getElementsByClassName(
      'add-tip'
    ) as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < addTipIcons.length; i++) {
      addTipIcons[i].addEventListener('click', () => {
        this.addTipShow();
      });
    }

    let editTipIcons = document.getElementsByClassName(
      'edit-tip'
    ) as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < editTipIcons.length; i++) {
      editTipIcons[i].addEventListener('click', () => {
        this.editTipShow();
      });
    }

    let deleteTipIcons = document.getElementsByClassName(
      'delete-tip'
    ) as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < deleteTipIcons.length; i++) {
      deleteTipIcons[i].addEventListener('click', () => {
        this.deleteTipShow();
      });
    }

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

  editTitleShow() {
    this.modalOverlay.classList.remove('hidden');
    this.titleAdd = false;
    this.titleEdit = true;
    this.tipAdd = false;
    this.tipEdit = false;
  }

  addTipShow() {
    this.modalOverlay.classList.remove('hidden');
    this.titleAdd = false;
    this.titleEdit = false;
    this.tipAdd = true;
    this.tipEdit = false;
  }

  editTipShow() {
    this.modalOverlay.classList.remove('hidden');
    this.titleAdd = false;
    this.titleEdit = false;
    this.tipAdd = false;
    this.tipEdit = true;
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
