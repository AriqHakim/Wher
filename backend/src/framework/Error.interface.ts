export class BaseError extends Error {
  private _code: number;

  constructor(code: number, message: string) {
    super(message);
    this._code = code;
  }

  get code() {
    return this._code;
  }

  set code(value: number) {
    this._code = value;
  }

  public toString(): string {
    return `${this.code}: ${this.message}`;
  }
}

export class BadRequestError extends BaseError {
  constructor(message: string) {
    super(400, message);
  }
}

export class UnauthorizedError extends BaseError {
  constructor(message: string) {
    super(401, message);
  }
}

export class NotFoundError extends BaseError {
  constructor(message: string) {
    super(404, message);
  }
}
