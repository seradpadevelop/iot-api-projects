CREATE TABLE "user" (
    "id" serial   NOT NULL,
    "email" text   NOT NULL,
    "user_name" text   NULL,
    CONSTRAINT "pk_user" PRIMARY KEY (
        "id"
     )
);