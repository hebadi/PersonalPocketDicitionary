PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE `dictionary` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL, `part_of_speech` TEXT NOT NULL, `definition` TEXT NOT NULL, `example` TEXT NOT NULL);
INSERT INTO dictionary VALUES(1,'Parable','noun','a story with life lessons used to illustrate a moral or spiritual lesson.','the para deez nuts on your chin. Gotem');
INSERT INTO dictionary VALUES(2,'Hyponatremia','noun','hypo- meaning less than normal, na- sodium, think of the element Na, and emia- related to blood. All together, it means insufficient sodium in the blood','yo if you''re raving tonight add some electrolytes in your water pack. Especially if you''re rolling watch out for hyponatremia');
INSERT INTO dictionary VALUES(3,'Venerable','adjective','the epithet we yearn for. It means respected','the great, the venerable, the honorable, Muhammad Ali');
COMMIT;