import { MigrationInterface, QueryRunner } from "typeorm";

export class FriendRequests1695715957297 implements MigrationInterface {
    name = 'FriendRequests1695715957297'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`CREATE TABLE \`friend_requests\` (\`id\` varchar(36) NOT NULL, \`status\` enum ('Pending', 'Accepted') NOT NULL, \`requester\` varchar(36) NULL, \`target\` varchar(36) NULL, PRIMARY KEY (\`id\`)) ENGINE=InnoDB`);
        await queryRunner.query(`ALTER TABLE \`friend_requests\` ADD CONSTRAINT \`FK_3804f1a47fbf4a10d3046bff8bd\` FOREIGN KEY (\`requester\`) REFERENCES \`users\`(\`id\`) ON DELETE CASCADE ON UPDATE CASCADE`);
        await queryRunner.query(`ALTER TABLE \`friend_requests\` ADD CONSTRAINT \`FK_7d355fcb7b9271f31710f0a8f41\` FOREIGN KEY (\`target\`) REFERENCES \`users\`(\`id\`) ON DELETE CASCADE ON UPDATE CASCADE`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE \`friend_requests\` DROP FOREIGN KEY \`FK_7d355fcb7b9271f31710f0a8f41\``);
        await queryRunner.query(`ALTER TABLE \`friend_requests\` DROP FOREIGN KEY \`FK_3804f1a47fbf4a10d3046bff8bd\``);
        await queryRunner.query(`DROP TABLE \`friend_requests\``);
    }

}
