import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TestService } from './test.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  public informatie: any;

  constructor(private service: TestService) {
  }
  
  async ngOnInit() {
    let response = await firstValueFrom(this.service.GetAll());
    this.informatie = await response;
    console.log(this.informatie);
  }
}
