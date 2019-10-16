CREATE TABLE "role" (
    "id" serial        NOT NULL,
    "name" text        NOT NULL,
    "permission_id" bigint NULL,
    "project_id" bigint NOT NULL,
    CONSTRAINT "pk_role" PRIMARY KEY (
        "id"
     )
);

ALTER TABLE "role" ADD CONSTRAINT "fk_role_project_id" FOREIGN KEY("project_id") REFERENCES "project" ("id");