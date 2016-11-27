from DataObjects.DatabaseObject import DatabaseObject
from decimal import Decimal, ROUND_DOWN

class Address(DatabaseObject):
	TABLE_NAME = "ADDRESSES"
	TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
					ID INT AUTO_INCREMENT,
					renterEmail VARCHAR(100),
					ownerEmail VARCHAR(100) NOT NULL,
					address VARCHAR(200) NOT NULL,
					spotType SMALLINT NOT NULL,
					isCovered BOOL NOT NULL,
					isRecurring BOOL NOT NULL,
					description VARCHAR(300),
					picturePath VARCHAR(300),
					latitude FLOAT, longitude FLOAT
					FOREIGN KEY (renterEmail) REFERENCES USERS(email),
					FOREIGN KEY (ownerEmail) REFERENCES USERS(email),
					PRIMARY KEY (ID)
				);'''.format(TABLE_NAME)