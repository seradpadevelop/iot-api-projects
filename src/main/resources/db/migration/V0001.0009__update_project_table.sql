ALTER TABLE "project"
  ADD "projectid" text;
  
UPDATE "project"
  SET "projectid" = '1' WHERE "id" = 1;
  
INSERT INTO "project"(name, project_manager, start_date, end_date, progress, projectid)
  VALUES ('Stairway PP', 'Katja', '2019-01-01', '2020-01-01', 15, '2');
