import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faLayerGroup, 
  faCertificate, 
  faLightbulb, 
  faUsers, 
  faHeadset, 
  faSignOutAlt,
  faUser,
  faGlobe
} from '@fortawesome/free-solid-svg-icons';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, FontAwesomeModule, NgClass],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  faLayerGroup = faLayerGroup;
  faCertificate = faCertificate;
  faLightbulb = faLightbulb;
  faUsers = faUsers;
  faHeadset = faHeadset;
  faGlobe = faGlobe;
  faSignOutAlt = faSignOutAlt;
  faUser = faUser;
}
