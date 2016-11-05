'''
Created on Jan 3, 2016

@author: henrylevy
'''
import MySQLdb, os
from _mysql_exceptions import ProgrammingError
from DataObjects.DatabaseObject import DatabaseObject
from Model.BaseManagers.ObjectStorageManager import ObjectStorageManager

class SQLDatabaseManager(object):
    DB_OBJECT_CLASS     = DatabaseObject
    TABLE_EXISTS_QUERY = """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_name = '{0}'
            """
    def __init__(self, host, user, password, port, db, dbObject):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        :type dbObject: DatabaseObject
        '''
        
        self.host     = host
        self.user     = user
        self.password = password
        self.port     = port
        self.database = db
        self.dbObject = dbObject
        
        self._db = MySQLdb.connect(host   = self.host,
                                   user   = self.user,
                                   passwd = self.password,
                                   port   = self.port,
                                   db     = self.database)
        self._cursor = self.db.cursor()
        print os.getenv('AWS_ACCESS_KEY_ID', '')
        # self._objStorageManager = ObjectStorageManager('parkhereapp')
        # TODO implement our own storage
    @property
    def objStorageManager(self):
        if not self._objStorageManager:
            self._objStorageManager = ObjectStorageManager('parkhereapp')
        return self._objStorageManager
    
    def __del__(self):
        if self.db:
            self.db.close()
        if self.cursor:
            self.cursor.close()
    
    @property
    def db(self):
        if not self._db:
            self._db = MySQLdb.connect(host   = self.host,
                                       user   = self.user,
                                       passwd = self.password,
                                       port   = self.port,
                                       db     = self.database)
            self._db.autocommit(True)
        return self._db
    
    @property
    def cursor(self):
        if not self._cursor:
            self._cursor = self.db.cursor()
        return self._cursor
    
    def deleteTable(self):
        '''
        deletes table
        :type tableName: str
        '''
        self.execute('''DROP TABLE {0}'''.format(self.DB_OBJECT_CLASS.TABLE_NAME))
        
    def cleanTable(self):
        '''
        deletes table and creates it again, empty
        :type tableName: str
        '''
        
        self.delete_table()
        self.create_table()
        
    def addIndex(self, tableName, index, isInt=False, database=None):
        '''
        :type tableName: str
        :type index: str
        :type isInt: bool
        '''
        
        if isInt:
            self.execute('''ALTER TABLE {0} ADD INDEX {1} ({1})'''.format(tableName, index), database=database)
        else:
            self.execute('''ALTER TABLE {0} ADD INDEX `{1}` (`{1}`)'''.format(tableName, index), database=database)
            
    def close(self):
        self.cursor.close()
        self.db.close()
    
    def execute(self, query, tries=0, time_out=10, database=None):
        '''
        :type query: str
        :type tries: int
        :type time_out: int
        '''
        print query
        try:
            db = MySQLdb.connect(host   = self.host,
                                 user   = self.user,
                                 passwd = self.password,
                                 port   = self.port,
                                 db     = (database or self.database))
            curr = db.cursor()
            curr.execute(query)
            vals = curr.fetchall()
            db.commit()
            db.close()
            return vals
        except ProgrammingError as e:
            if tries<time_out:
                return self.execute(query, tries+1, time_out)
            else:
                raise e
            
    def tableExists(self, tablename, database=None):
        '''
        :type tablename: str
        '''
        database = database or self.database
        return bool(len(self.execute(self.TABLE_EXISTS_QUERY.format(str(tablename).replace('\'', '')), database=database)[0]))
    
    def createDatabase(self, database):
        query = '''
            CREATE DATABASE IF NOT EXISTS {0};
        '''
        self.execute(query.format(database))

    def insertIntoTable(self, dbObject):
        '''
        :type dbObject: DatabaseObject
        '''
        self.createTable()
        self.execute(dbObject.asInsertStatement())

    def createTable(self):
        '''
        creates table if it doesn't already exist
        '''
        if not self.tableExists(self.DB_OBJECT_CLASS.TABLE_NAME, self.database):
            self.execute(self.DB_OBJECT_CLASS.TABLE_CREATE_STATEMENT, database=self.database)
            
