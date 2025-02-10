import { Component, OnInit, OnDestroy } from '@angular/core';
import { trigger, transition, style, animate, query, group } from '@angular/animations';
import { RouterOutlet } from '@angular/router';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('routeAnimations', [
      transition('* <=> *', [

        query(':enter, :leave', [
          style({
            position: 'absolute',
            width: '100%',
            transform: 'translateX(100%)',
            opacity: 0
          })
        ], { optional: true }),


        group([
          query(':leave', [
            animate('1100ms ease-in', style({
              transform: 'translateX(-100%)',
              opacity: 0
            }))
          ], { optional: true }),

          query(':enter', [
            style({ transform: 'translateX(100%)', opacity: 0 }),
            animate('1100ms ease-out', style({
              transform: 'translateX(0%)',
              opacity: 1
            }))
          ], { optional: true })
        ])
      ])
    ])
  ]
})
export class AppComponent implements OnInit, OnDestroy {
  backgroundImages: string[] = [
    '../../assets/background/back.jpg',

  ];
  currentBackground: string = this.backgroundImages[0];
  private intervalId: any;

  ngOnInit() {
    let index = 0;
    this.intervalId = setInterval(() => {
      index = (index + 1) % this.backgroundImages.length;
      this.currentBackground = this.backgroundImages[index];
    }, 10000); // 10000 ms is 10 seconds


  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }
}
