export class DatabaseError extends Error {
  
  constructor(errorMessage?: string) {
    super(errorMessage);
  }

}