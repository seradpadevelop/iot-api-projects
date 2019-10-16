CREATE TABLE "role_assignment" (
    "id" serial NOT NULL,
    "user_id" bigint NOT NULL,
    "role_id" bigint NOT NULL,
    "project_id" bigint  NULL,
    CONSTRAINT "pk_role_assignment" PRIMARY KEY (
        "id"
     )
);

ALTER TABLE "role_assignment" ADD CONSTRAINT "fk_role_assignment_user_id" FOREIGN KEY("user_id") REFERENCES "user" ("id");
ALTER TABLE "role_assignment" ADD CONSTRAINT "fk_role_assignment_role_id" FOREIGN KEY("role_id") REFERENCES "role" ("id");
ALTER TABLE "role_assignment" ADD CONSTRAINT "fk_role_assignment_project_id" FOREIGN KEY("project_id") REFERENCES "project" ("id");