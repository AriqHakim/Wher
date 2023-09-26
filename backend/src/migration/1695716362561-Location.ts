import { MigrationInterface, QueryRunner } from "typeorm";

export class Location1695716362561 implements MigrationInterface {
    name = 'Location1695716362561'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`CREATE TABLE \`locations\` (\`id\` varchar(36) NOT NULL, \`date\` datetime NOT NULL, \`lat\` double NOT NULL, \`lon\` double NOT NULL, \`user\` varchar(36) NULL, UNIQUE INDEX \`REL_0f91ef85d67199360af0b43e3b\` (\`user\`), PRIMARY KEY (\`id\`)) ENGINE=InnoDB`);
        await queryRunner.query(`ALTER TABLE \`locations\` ADD CONSTRAINT \`FK_0f91ef85d67199360af0b43e3b1\` FOREIGN KEY (\`user\`) REFERENCES \`users\`(\`id\`) ON DELETE CASCADE ON UPDATE CASCADE`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE \`locations\` DROP FOREIGN KEY \`FK_0f91ef85d67199360af0b43e3b1\``);
        await queryRunner.query(`DROP INDEX \`REL_0f91ef85d67199360af0b43e3b\` ON \`locations\``);
        await queryRunner.query(`DROP TABLE \`locations\``);
    }

}
