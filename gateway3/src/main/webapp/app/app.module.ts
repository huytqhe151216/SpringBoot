import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Gateway3SharedModule } from 'app/shared/shared.module';
import { Gateway3CoreModule } from 'app/core/core.module';
import { Gateway3AppRoutingModule } from './app-routing.module';
import { Gateway3HomeModule } from './home/home.module';
import { Gateway3EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Gateway3SharedModule,
    Gateway3CoreModule,
    Gateway3HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Gateway3EntityModule,
    Gateway3AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class Gateway3AppModule {}
