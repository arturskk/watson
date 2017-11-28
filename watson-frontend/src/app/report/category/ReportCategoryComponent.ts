import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'report-category-component',
  template: `
    <h1>Raport wydatków - kategorie</h1>
    <div>
      Od:
      <input [(ngModel)]="dateFrom"/>
      Do:
      <input [(ngModel)]="dateTo"/>
      <button (click)="refreshReport()">Pobierz</button>
    </div>
    <hr/>
    <div *ngIf="report">
      <report-category-item [category]="report.rootCategory">
      </report-category-item>
    </div>
  `,
  styles: [
      `
      :host {
        display: block
      }
    `
  ]
})
export class ReportCategoryComponent implements OnInit {

  dateFrom: string;
  dateTo: string;
  report: any;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    const today = new Date().toISOString().substr(0, 10);
    this.dateFrom = today;
    this.dateTo = today;
    this.refreshReport();
  }

  refreshReport() {
    this.report = null;
    this.httpClient
      .get<any>(`/api/v1/report/category/?from=${this.dateFrom}&to=${this.dateTo}`)
      .subscribe(data => this.report = data);
  }

}
