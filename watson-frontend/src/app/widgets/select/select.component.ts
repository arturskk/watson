import {
  Component,
  ContentChild,
  ElementRef,
  EventEmitter,
  forwardRef,
  Input,
  Output,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

@Component({
  selector: 'ws-select',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ],
  template: `
    <div class="ws-select-container">
      <div class="search-box-wrapper" (click)="onSearchBoxWrapperClicked()">
        <span class="current-value" *ngIf="value">{{getValueAsDisplayField()}}</span>
        <input
          #searchBox
          [disabled]="disabled"
          [class.hidden]="value"
          [placeholder]="getPlaceholder()"
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
          <div [class.active]="index === currentIndex" (mousedown)="itemClicked(item)" class="dropdown-item">
            <ng-template [ngTemplateOutlet]="itemTemplate"
                         [ngTemplateOutletContext]="{$implicit: item, markSearchText: markSearchText.bind(this), newItem: item === newValuePlaceholder}">
            </ng-template>
          </div>
        </div>
        <div *ngIf="rawData.length > itemsLimit && filtered.length >= itemsLimit"
             class="dropdown-item filter-to-show-more">
          Filtruj aby zobaczyć kolejne...
        </div>
      </div>
    </div>
  `,
  styleUrls: [
    'select.component.scss'
  ]
})
export class SelectComponent<T> implements ControlValueAccessor {

  static newValuePlaceholderTemplate = {newValue: true};
  dropdown = false;
  filtered: T[] = [];
  currentIndex = -1;
  newValuePlaceholder: any = {...SelectComponent.newValuePlaceholderTemplate};
  rawData: T[] = [];
  currentSearchText = undefined;

  @Output() onChange = new EventEmitter<T>();
  @Output() onTouch = new EventEmitter();
  @Input() value: T = undefined;
  @Input() allowNewValues = true;
  @Input() disabled = false;
  @Input() placeholder = '';
  @Input() filter: (item: T, searchText: string) => boolean;
  @Input() displayField: string;
  @Input() itemsLimit = 25;
  @Input() skipValueWriting = false;
  @ContentChild('listItem') itemTemplate: TemplateRef<any>;
  @ViewChild('searchBox', {read: ElementRef}) searchBox: ElementRef;

  constructor() {
    this.filter = (item, searchText) => item[this.displayField].toLocaleLowerCase().indexOf(searchText.toLocaleLowerCase()) >= 0;
  }

  @Input()
  set data(data) {
    this.rawData = data;
    this.filtered = this.rawData.slice(0, this.itemsLimit);
  }

  onSearchBoxWrapperClicked() {
    this.searchBox.nativeElement.focus();
  }

  onSearchBoxFocus() {
    this.dropdown = true;
  }

  onSearchBoxBlur() {
    this.onTouch.emit();
    if (this.currentIndex >= 0) {
      this.changeValue(this.filtered[this.currentIndex]);
    }
    this.clearUi();
  }

  itemClicked(item) {
    this.changeValue(item);
    this.clearUi();
  }

  onSearchBoxEnter() {
    if (!this.dropdown) {
      this.dropdown = true;
    } else if (this.currentIndex >= 0) {
      this.changeValue(this.filtered[this.currentIndex]);
      this.clearUi();
    } else {
      this.clearUi();
    }
  }

  onSearchBoxEscape() {
    this.clearUi();
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
      this.changeValue(undefined);
      this.clearUi();
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
    this.filtered = this.rawData.filter(item => this.filter(item, this.currentSearchText)).slice(0, this.itemsLimit);
    this.currentIndex = -1;
    if (this.allowNewValues) {
      if (this.currentSearchText) {
        this.filtered.push(this.newValuePlaceholder);
        this.newValuePlaceholder[this.displayField] = this.currentSearchText;
        if (this.filtered.length === 1) {
          this.currentIndex = 0;
        }
      } else {
        this.newValuePlaceholder[this.displayField] = undefined;
      }
    }
  }

  getValueAsDisplayField() {
    return this.value ? this.value[this.displayField] : '';
  }

  markSearchText(input: string): string {
    if (!this.currentSearchText) {
      return input;
    }

    const matchStart = input.toLocaleLowerCase().indexOf(this.currentSearchText.toLocaleLowerCase());
    if (matchStart < 0) {
      return input;
    }

    const beforeMatch = input.substr(0, matchStart);
    const match = input.substr(matchStart, this.currentSearchText.length);
    const afterMatch = input.substr(matchStart + this.currentSearchText.length);

    return `${beforeMatch}<span class="select-item-match-part">${match}</span>${afterMatch}`;
  }

  writeValue(value: T): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange.subscribe(fn);
  }

  registerOnTouched(fn: any): void {
    this.onTouch.subscribe(fn);
  }

  getPlaceholder() {
    if (!this.value) {
      return this.placeholder;
    } else {
      return '';
    }
  }

  private clearUi() {
    this.newValuePlaceholder = {...SelectComponent.newValuePlaceholderTemplate};
    this.dropdown = false;
    this.currentSearchText = undefined;
    this.filtered = this.rawData.slice(0, this.itemsLimit);
    this.currentIndex = -1;
    this.searchBox.nativeElement.value = '';
  }

  private changeValue(value) {
    if (!this.skipValueWriting) {
      this.value = value;
    }
    this.onChange.emit(value);
  }

}
