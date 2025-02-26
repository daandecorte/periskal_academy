import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TestService } from './test.service';
import { firstValueFrom } from 'rxjs';
import { NavbarComponent } from './navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';

  public informatie: any;

  constructor(private service: TestService) {
  }
  
  async ngOnInit() {
    let response = await firstValueFrom(this.service.GetAll());
    this.informatie = await response;
    console.log(this.informatie);
  }
}
