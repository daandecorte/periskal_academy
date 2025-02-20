import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';

import { NavbarComponent } from './navbar.component';
import { Router } from '@angular/router';
import { CertificatesComponent } from '../certificates/certificates.component';
import { ModulesComponent } from '../modules/modules.component';
import { SkippersComponent } from '../skippers/skippers.component';
import { SupportComponent } from '../support/support.component';
import { TipsAndTricksComponent } from '../tips-and-tricks/tips-and-tricks.component';
import { UserManagementComponent } from '../user-management/user-management.component';
import { routes } from '../app.routes';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  let router: Router;
  let location: Location;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes), NavbarComponent, CertificatesComponent, ModulesComponent, SkippersComponent, SupportComponent, TipsAndTricksComponent, UserManagementComponent],
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    location = TestBed.inject(Location);

    fixture.detectChanges();

    router.initialNavigation();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should navigate to /modules', async () => {
    await router.navigate(['/modules']);
    expect(location.path()).toBe('/modules');
  });

  it('should navigate to /certificates', async () => {
    await router.navigate(['/certificates']);
    expect(location.path()).toBe('/certificates');
  });

  it('should navigate to /tips-and-tricks', async () => {
    await router.navigate(['/tips-and-tricks']);
    expect(location.path()).toBe('/tips-and-tricks');
  });

  it('should navigate to /user-management', async () => {
    await router.navigate(['/user-management']);
    expect(location.path()).toBe('/user-management');
  });

  it('should navigate to /skippers', async () => {
    await router.navigate(['/skippers']);
    expect(location.path()).toBe('/skippers');
  });

  it('should navigate to /support', async () => {
    await router.navigate(['/support']);
    expect(location.path()).toBe('/support');
  });

  it('should redirect empty path to /modules', async () => {
    await router.navigate(['']);
    expect(location.path()).toBe('/modules');
  });

  it('should redirect unknown path to /modules', async () => {
    await router.navigate(['/unknown-route']);
    expect(location.path()).toBe('/modules');
  });
});

