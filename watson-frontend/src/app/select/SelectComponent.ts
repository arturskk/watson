import {Component, ContentChild, ElementRef, EventEmitter, forwardRef, Input, Output, TemplateRef, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

@Component({
  selector: 'select-component',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ],
  template: `
    <div>
      <div class="search-box-wrapper" (click)="onSearchBoxWrapperClicked()">
        <span class="current-value" *ngIf="value">{{getValueAsDisplayField()}}</span>
        <input
          #searchBox
          [class.hidden]="this.value"
          [placeholder]="!this.value && placeholder || ''"
          (focus)="onSearchBoxFocus()"
          (blur)="onSearchBoxBlur()"
          (keydown.enter)="onSearchBoxEnter()"
          (keydown.escape)="onSearchBoxEscape()"
          (keydown.arrowup)="onSearchBoxArrowUp()"
          (keydown.arrowdown)="onSearchBoxArrowDown()"
          (keydown.backspace)="onSearchBoxBackspace()"
          (input)="onSearchBoxFilter()"
        />
      </div>
      <div class="dropdown" *ngIf="dropdown">
        <div *ngFor="let item of filtered; let index = index">
          <div [class.active]="index === currentIndex" (mousedown)="itemClicked(item)">
            <ng-template [ngTemplateOutlet]="itemTemplate"
                         [ngTemplateOutletContext]="{$implicit: item, markSearchText: markSearchText.bind(this), newItem: item === newValuePlaceholder}"></ng-template>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
      `
      :host {
        display: block;
        width: 172px;
        border: 1px solid deeppink;
      }

      .dropdown {
        position: absolute;
        background-color: aliceblue;
      }

      .active {
        background-color: deepskyblue;
      }

      :host ::ng-deep .match {
        font-weight: bold;
      }

      .search-box-wrapper {
        display: flex;
      }

      input.hidden {
        width: 5px;
      }

      input, input:focus {
        margin: 0;
        padding: 0;
        border: none;
        outline: none;
      }
    `
  ]
})
export class SelectComponent implements ControlValueAccessor {

  dropdown = false;
  value = null;
  filtered = [];
  currentIndex = 0;
  newValuePlaceholder = {};
  rawData = [];
  currentSearchText = null;

  @Output() valueChange = new EventEmitter();
  @Input() placeholder: string = undefined;
  @Input() filter: (item: any, searchText: string) => boolean;
  @Input() displayField: string;
  @ContentChild('listItem') itemTemplate: TemplateRef<any>;
  @ViewChild('searchBox', {read: ElementRef}) searchBox: ElementRef;

  constructor() {
    this.filter = (item, searchText) => item[this.displayField].toLocaleLowerCase().indexOf(searchText.toLocaleLowerCase()) >= 0;
  }

  @Input()
  set data(data) {
    this.rawData = data;
    this.filtered = this.rawData;
  }

  onSearchBoxWrapperClicked() {
    this.searchBox.nativeElement.focus();
  }

  onSearchBoxFocus() {
    this.dropdown = true;
  }

  onSearchBoxBlur() {
    this.dropdown = false;
  }

  itemClicked(item) {
    this.dropdown = false;
    this.value = item;
    this.valueChange.emit(this.value);
    this.clearUi();
  }

  onSearchBoxEnter($event) {
    if (!this.dropdown) {
      this.dropdown = true;
    } else {
      this.value = this.filtered[this.currentIndex];
      this.valueChange.emit(this.value);
      this.clearUi();
    }
  }

  onSearchBoxEscape() {
    this.dropdown = false;
  }

  onSearchBoxArrowUp() {
    if (!this.dropdown) {
      this.dropdown = true;
    } else if (this.currentIndex > 0) {
      this.currentIndex -= 1;
    }
  }

  onSearchBoxArrowDown() {
    if (!this.dropdown) {
      this.dropdown = true;
    } else if (this.currentIndex + 1 < this.filtered.length) {
      this.currentIndex += 1;
    }
  }

  onSearchBoxBackspace() {
    if (!this.currentSearchText && this.value) {
      this.value = null;
    }
  }

  onSearchBoxFilter() {
    if (this.value) {
      this.searchBox.nativeElement.value = '';
      return;
    }
    if (!this.dropdown) {
      this.dropdown = true;
    }
    this.currentSearchText = this.searchBox.nativeElement.value;
    this.filtered = [];
    if (this.currentSearchText) {
      this.filtered.push(this.newValuePlaceholder);
      this.newValuePlaceholder[this.displayField] = this.currentSearchText;
    } else {
      this.newValuePlaceholder[this.displayField] = null;
    }
    this.filtered.push(...this.rawData.filter(item => this.filter(item, this.currentSearchText)));
  }

  getValueAsDisplayField() {
    return this.value ? this.value[this.displayField] : '';
  }

  markSearchText(input: string): string {
    if (this.currentSearchText) {
      const matchStart = input.toLocaleLowerCase().indexOf(this.currentSearchText.toLocaleLowerCase());

      const beforeMatch = input.substr(0, matchStart);
      const match = input.substr(matchStart, this.currentSearchText.length);
      const afterMatch = input.substr(matchStart + this.currentSearchText.length);

      return `${beforeMatch}<span class="match">${match}</span>${afterMatch}`;
    } else {
      return input;
    }
  }

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.valueChange.subscribe(fn);
  }

  registerOnTouched(fn: any): void {
  }

  private clearUi() {
    this.newValuePlaceholder = {};
    this.dropdown = false;
    this.currentSearchText = null;
    this.filtered = this.rawData;
    this.currentIndex = 0;
    this.searchBox.nativeElement.value = '';
  }

}
