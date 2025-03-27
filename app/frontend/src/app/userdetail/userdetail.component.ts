import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

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
  imports: [CommonModule],
  templateUrl: './userdetail.component.html',
  styleUrl: './userdetail.component.css'
})
export class UserdetailComponent {
 
}
