import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NewTrainingService {
  public currentIndex: number=0;
  private _newTraining: INewTraining = {
    id: -1,
    title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    active: false,
    modules: [],
    exam: {
      id: -1,
      passing_score: 0,
      max_attempts: 0,
      time: 0,
      question_amount: 0,
      questions: [],
    },
    certificate: {
      id: -1,
      validity_period: 1,
      price: 0,
    },
  };
  setActiveState(state: boolean) {
    this._newTraining.active=state;
  }
  resetTraining() {
    this._newTraining = {
      id: -1,
      title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      active: false,
      modules: [],
      exam: {
        id: -1,
        passing_score: 0,
        max_attempts: 0,
        time: 0,
        question_amount: 0,
        questions: [],
      },
      certificate: {
        id: -1,
        validity_period: 1,
        price: 0,
      },
    };

    this._editModuleIndex = -1;
    this.imagePreviews = [];
    this.videoPreviews = [];
    this.removedContentsIndex = [];
    this.removedModulesIndex = [];
  }

  public get newTraining() {
    return this._newTraining;
  }

  public set newTraining(value: INewTraining) {
    this._newTraining = value;
  }

  private _newModule: IModule = {
    id: -1,
    title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    content: [],
    questions: [],
  };

  public get newModule() {
    return this._newModule;
  }

  public set newModule(value: IModule) {
    this._newModule = value;
  }

  private _editModuleIndex: number = -1;

  public editModuleIndex(index: number) {
    this._editModuleIndex = index;
  }

  public get editModuleIndexGet() {
    return this._editModuleIndex;
  }

  removedModulesIndex: number[] = [];
  removedContentsIndex: number[] = [];

  public resetModule() {
    this._newModule = {
      id: -1,
      title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      content: [],
      questions: [],
    };

    this.setButtonSelectedLanguage('ENGLISH');
  }

  public saveModule() {
    if (this._editModuleIndex == -1)
      this._newTraining.modules.push(this._newModule);
    else {
      this._newTraining.modules.splice(
        this._editModuleIndex,
        1,
        this._newModule
      );
    }

    this.resetModule();
  }

  public addModuleQuestion() {
    this._newModule.questions.push({
      id: -1,
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      question_options: [],
    });
  }

  public addModuleQuestionOption(index: number) {
    this._newModule.questions[index].question_options.push({
      id: -1,
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      correct: false,
    });
  }

  public deleteModuleQuestionOption(indexQ: number, indexO: number) {
    this._newModule.questions[indexQ].question_options.splice(indexO, 1);
  }

  public addExamQuestion() {
    this._newTraining.exam.questions.push({
      id: -1,
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      question_options: [],
    });
  }

  public addExamQuestionOption(index: number) {
    this._newTraining.exam.questions[index].question_options.push({
      id: -1,
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      correct: false,
    });
  }

  public deleteExamQuestionOption(indexQ: number, indexO: number) {
    this._newTraining.exam.questions[indexQ].question_options.splice(indexO, 1);
  }

  private _buttonSelectedLanguage$ = new BehaviorSubject<keyof ITranslated>(
    'ENGLISH'
  );
  public get buttonSelectedLanguage$() {
    return this._buttonSelectedLanguage$.asObservable();
  }
  public setButtonSelectedLanguage(value: keyof ITranslated) {
    this._buttonSelectedLanguage$.next(value);
  }

  public videoPreviews: ITranslated[][] = [];
  public imagePreviews: ITranslated[][] = [];

  addVideoPreviewModule() {
    this.videoPreviews.push([]);
  }

  addVideoPreview() {
    let addVideoIndex = this.newTraining.modules.length;
    if (this._editModuleIndex != -1) addVideoIndex = this._editModuleIndex;
    this.videoPreviews[addVideoIndex].push({
      ENGLISH: '',
      DUTCH: '',
      GERMAN: '',
      FRENCH: '',
    });
  }

  addImagePreviewModule() {
    this.imagePreviews.push([]);
  }

  addImagePreview() {
    let addImageIndex = this.newTraining.modules.length;
    if (this._editModuleIndex != -1) addImageIndex = this._editModuleIndex;
    this.imagePreviews[addImageIndex].push({
      ENGLISH: '',
      DUTCH: '',
      GERMAN: '',
      FRENCH: '',
    });
  }
}

export interface ITranslated {
  DUTCH: string | File;
  ENGLISH: string | File;
  FRENCH: string | File;
  GERMAN: string | File;
}

export interface INewTraining {
  id: number;
  title: ITranslated;
  description: ITranslated;
  modules: IModule[];
  exam: IExam;
  active: boolean;
  certificate: ICertificate;
}

export interface IModule {
  id: number;
  title: ITranslated;
  description: ITranslated;
  content: IContent[];
  questions: IQuestion[];
}

export interface IContent {
  id: number;
  content_type: ContentType;
  reference: ITranslated;
}

export enum ContentType {
  TEXT = 'TEXT',
  PICTURE = 'PICTURE',
  VIDEO = 'VIDEO',
}

export interface IExam {
  id: number;
  passing_score: number;
  max_attempts: number;
  time: number;
  question_amount: number;
  questions: IQuestion[];
}

export interface IQuestion {
  id: number;
  text: ITranslated;
  question_options: IQuestionOption[];
}

export interface IQuestionOption {
  id: number;
  text: ITranslated;
  correct: boolean;
}

export interface ICertificate {
  id: number;
  validity_period: number; //years
  price: number;
}
