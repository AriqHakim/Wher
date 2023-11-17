export interface JWTConfig {
  secret_key: string;
  jwtDataAction(payload: any): Promise<any>;
}
