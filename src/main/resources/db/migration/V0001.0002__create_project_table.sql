CREATE TABLE "project" (
    "id" serial            NOT NULL,
    "name" text            NOT NULL,
    "project_manager" text NOT NULL,
    "start_date" date      NOT NULL,
    "end_date" date        NOT NULL,
    "progress" int         NOT NULL DEFAULT 0,
    CONSTRAINT "pk_project" PRIMARY KEY (
        "id"
     )
);