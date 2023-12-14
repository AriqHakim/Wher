import AppDataSource from '../src/config/orm.config';

export class TestHelper {
  private static _instance: TestHelper;

  private constructor() {}

  public static get instance(): TestHelper {
    if (!this._instance) this._instance = new TestHelper();

    return this._instance;
  }

  async setupTestDB() {
    await AppDataSource.initialize();
  }

  async teardownTestDB() {
    const entities = AppDataSource.entityMetadatas;
    for (const entity of entities) {
      const repository = AppDataSource.getRepository(entity.name);
      await repository.delete({});
    }
    AppDataSource.destroy();
  }
}
