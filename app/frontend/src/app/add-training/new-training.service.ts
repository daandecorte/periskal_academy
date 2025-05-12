import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NewTrainingService {
  private _newTraining: INewTraining = {
    title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
    isActive: false,
    modules: [],
    exam: {
      passingScore: 0,
      maxAttempts: 0,
      time: 0,
      questionAmount: 0,
      questions: [],
    },
    certificate: {
      validityPeriod: 1,
      price: 0,
    },
  };

  public get newTraining() {
    return this._newTraining;
  }

  public set newTraining(value: INewTraining) {
    this._newTraining = value;
  }

  private _newModule: IModule = {
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

    this._newModule = {
      title: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      description: { ENGLISH: '', FRENCH: '', DUTCH: '', GERMAN: '' },
      content: [],
      questions: [],
    };

    this.setButtonSelectedLanguage('ENGLISH');
  }

  public addModuleQuestion() {
    this._newModule.questions.push({
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      questionOptions: [],
    });
  }

  public addModuleQuestionOption(index: number) {
    this._newModule.questions[index].questionOptions.push({
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      is_correct: false,
    });
  }

  public deleteModuleQuestionOption(indexQ: number, indexO: number) {
    this._newModule.questions[indexQ].questionOptions.splice(indexO, 1);
  }

  public addExamQuestion() {
    this._newTraining.exam.questions.push({
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      questionOptions: [],
    });
  }

  public addExamQuestionOption(index: number) {
    this._newTraining.exam.questions[index].questionOptions.push({
      text: {
        ENGLISH: '',
        DUTCH: '',
        FRENCH: '',
        GERMAN: '',
      },
      is_correct: false,
    });
  }

  public deleteExamQuestionOption(indexQ: number, indexO: number) {
    this._newTraining.exam.questions[indexQ].questionOptions.splice(indexO, 1);
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
}

export interface INewTraining {
  title: ITranslated;
  description: ITranslated;
  isActive: boolean;
  modules: IModule[];
  exam: IExam;
  certificate: ICertificate;
}

interface ITranslated {
  ENGLISH: string | File;
  FRENCH: string | File;
  DUTCH: string | File;
  GERMAN: string | File;
}

interface IModule {
  title: ITranslated;
  description: ITranslated;
  content: IContent[];
  questions: IQuestion[];
}

interface IContent {
  contentType: ContentType;
  reference: ITranslated;
}

enum ContentType {
  TEXT,
  PICTURE,
  VIDEO,
}

interface IExam {
  passingScore: number;
  maxAttempts: number;
  time: number;
  questionAmount: number;
  questions: IQuestion[];
}

interface IQuestion {
  text: ITranslated;
  questionOptions: IQuestionOption[];
}

interface IQuestionOption {
  text: ITranslated;
  is_correct: boolean;
}

interface ICertificate {
  validityPeriod: number; //years
  price: number;
}
