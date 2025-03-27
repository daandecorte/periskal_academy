import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

interface Module {
  name: string;
  assignedDate: Date;
  status: 'In Progress' | 'Completed';
  courseProgress: number;
  lastActivity?: Date;
  examStatus?: {
    firstAttempt?: Date;
    secondAttempt?: Date;
    result?: 'Failed' | 'Passed';
  };
}

@Component({
  selector: 'app-userdetail',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css'
})
export class UserdetailComponent {
 
}
