import { MigrationInterface, QueryRunner } from "typeorm";

export class UserFriends1695715664194 implements MigrationInterface {
    name = 'UserFriends1695715664194'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`CREATE TABLE \`user_friends\` (\`id\` varchar(36) NOT NULL, \`user_1\` varchar(36) NULL, \`user_2\` varchar(36) NULL, PRIMARY KEY (\`id\`)) ENGINE=InnoDB`);
        await queryRunner.query(`ALTER TABLE \`user_friends\` ADD CONSTRAINT \`FK_4c11af7835c63b1a877611857a5\` FOREIGN KEY (\`user_1\`) REFERENCES \`users\`(\`id\`) ON DELETE CASCADE ON UPDATE CASCADE`);
        await queryRunner.query(`ALTER TABLE \`user_friends\` ADD CONSTRAINT \`FK_9a3899a41bdf50f1497567955fd\` FOREIGN KEY (\`user_2\`) REFERENCES \`users\`(\`id\`) ON DELETE CASCADE ON UPDATE CASCADE`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE \`user_friends\` DROP FOREIGN KEY \`FK_9a3899a41bdf50f1497567955fd\``);
        await queryRunner.query(`ALTER TABLE \`user_friends\` DROP FOREIGN KEY \`FK_4c11af7835c63b1a877611857a5\``);
        await queryRunner.query(`DROP TABLE \`user_friends\``);
    }

}
