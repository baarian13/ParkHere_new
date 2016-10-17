'''
Created on Jan 3, 2016

@author: henrylevy
'''
import MySQLdb
from _mysql_exceptions import ProgrammingError

class SQLDatabaseManager(object):
    def __init__(self, host, user, password, port, db, dbObject):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        :type dbObject: User.User or Picture.Picture
        '''
        
        self.host = host
        self.user = user
        self.password = password
        self.port = port
        self.database = db
        self.dbObject = dbObject
        
        self._db = MySQLdb.connect(host = self.host,
                                user = self.user,
                                passwd = self.password,
                                port = self.port,
                                db = self.database)
        self._cursor = self.db.cursor()
    
    def __del__(self):
        self.db.close()
        self.cursor.close()
    
    @property
    def db(self):
        if not self._db:
            self._db = MySQLdb.connect(host = self.host,
                                user = self.user,
                                passwd = self.password,
                                port = self.port,
                                db = self.database)
            self._db.autocommit(True)
        return self._db
    
    @property
    def cursor(self):
        if not self._cursor:
            self._cursor = self.db.cursor()
        return self._cursor
    
    def other_cursor(self, database):
        _db = MySQLdb.connect(host = self.host,
                                user = self.user,
                                passwd = self.password,
                                port = self.port,
                                db = database or self.database)
        _db.autocommit(True)
        return _db.cursor()
    
    def delete_table(self, tableName):
        '''
        deletes table
        :type tableName: str
        '''
        
        self.execute('''DROP TABLE {0}'''.format(tableName))
        
    def clean_table(self, tableName):
        '''
        deletes table and creates it again, empty
        :type tableName: str
        '''
        
        self.delete_table(tableName)
        self.create_table(tableName, self.dbObject.DB_FIELDS, self.dbObject.DB_INDEX_FIELDS)
        
    def add_index(self, tableName, index, isInt=False, database=None):
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
        
        try:
            db = MySQLdb.connect(host = self.host,
                                user = self.user,
                                passwd = self.password,
                                port = self.port,
                                db = (database or self.database))
            curr = db.cursor()
            curr.execute(query)
            vals = curr.fetchall()
            db.commit()
            db.close()
            curr.close()
            return vals
        except ProgrammingError as e:
            if tries<time_out:
                return self.execute(query, tries+1, time_out)
            else:
                raise e
            
    def check_table_exists(self, tablename, database=None):
        '''
        :type tablename: str
        '''
        database = database or self.database
        if self.execute("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_name = '{0}'
            """.format(str(tablename).replace('\'', '\'\'')), database=database)[0][0] == 1:
            return True
        return False
    
    def create_database(self, database):
        query = '''
            CREATE DATABASE IF NOT EXISTS {0};
        '''
        print query.format(database)
        self.execute(query.format(database))
    
    def create_table(self, gameId, dbfields, indexFields, tableId=None, database=None):
        '''
        creates table if it doesn't already exist
        :type gameId: str
        :type dbfields: dict
        :type indexFields: dict
        '''
        if database:
            self.create_database(database)
        database = database or self.database
        if not self.check_table_exists(gameId, database):
            fields = ''
            for field, t in dbfields.iteritems():
                if field != self.dbObject.PRIMARY_KEY_NAME: 
                    fields += ' {0} {1},'.format(field, t)
            fields = fields[:-1]
            if not tableId:
                if str(gameId).isdigit():
                    gameId = '`{0}`'.format(gameId)
                query = '''CREATE TABLE IF NOT EXISTS {0}(
                        {2},
                        {1});'''.format(gameId, fields, self.dbObject.PRIMARY_KEY or 'ID INT PRIMARY KEY AUTO_INCREMENT')
                self.execute(query, database=database)
            else:
                self.execute('''create table IF NOT EXISTS {0}(
                        {1} PRIMARY KEY,
                        {2});'''.format(int(gameId), gameId, fields), database=database)
            for field, isInt in indexFields.iteritems():
                try:
                    self.add_index(gameId, field, isInt, database=database)
                except: pass
    
    def add_other_table(self, tableName, dbObject, database):
        self.create_table(tableName, dbObject.DB_FIELDS,
                          dbObject.DB_INDEX_FIELDS, database=database)
    
    def insert_into_table(self, tableName, row, database=None):
        '''
        :type tableName: str
        :type row: dict
        '''
        schema = self.dbObject.DB_FIELDS
        schema.pop('ID', None)
        self.create_table(tableName, schema, self.dbObject.DB_INDEX_FIELDS)
        fields = ''
        values = ''
        for field in self.dbObject.DB_FIELDS.iterkeys():
            fields += ' {0},'.format(field)
            if isinstance(row[field], str):
                values += '\n \'{0}\','.format(row[field])
            else:
                values += '\n {0},'.format((row[field]
                                            if row[field] or row[field] == 0
                                            else '\'N.A.\''))
        fields = fields[:-1]
        values = values[:-1]
        print """INSERT INTO {0} ({1}) 
                    VALUES ({2});""".format(tableName, fields, values)
        self.execute("""INSERT INTO {0} ({1}) 
                    VALUES ({2});""".format(tableName, fields, values))
            
    def general_select_from_table(self, tableName, selectFields, conditions=None):
        '''
        :type tableName: str
        :type selectFields: list
        :type conditions: dict
        '''
        
        fields = ''
        for fields in selectFields:
            fields += ' {0},'.format(fields)
        fields = fields[:-1]
        
        statement = 'select {0} from {1}'.format(fields, tableName)
        
        if conditions:
            cond = ''
            for field, val in conditions.iteritems():
                cond += ' {0} = {1} and '.format(field, val)
            cond = cond[:-4]
            statement += ' where {0}'.format(cond)
        
        return self.cursor.execute(statement)
