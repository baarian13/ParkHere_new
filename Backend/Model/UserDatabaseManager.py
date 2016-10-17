'''
Created on Jan 3, 2016

@author: henrylevy
'''

from Backend.Model.BaseManagers.DatabaseManagerBase import SQLDatabaseManager
from Backend.FunctionalUtils import saltPassword, createSalt

class SQLUserDatabaseManager(SQLDatabaseManager):
    
    def __init__(self, host, user, password, port, db, userType):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        '''
        
        super(SQLUserDatabaseManager, self).__init__(host, user, password,
                                                 port, db, userType)
    
    def add_index(self, index, isInt=False):
        '''
        :type index: str
        :type isInt: bool
        '''
        
        self.add_index(self.dbObject.tableName, index, isInt)
    
    def createSalt(self):
        return createSalt()
    
    def get_salt(self, username):
        '''
        :type username: str
        '''
        
        query = '''select salt from {0}
                    where {1} = \'{2}\''''.format(self.dbObject.tableName,
                                                  self.dbObject.PRIMARY_KEY_NAME,
                                                  username)
        self.cursor.execute(query)
        return self.cursor.fetchall()[0][0]
    
    def hash(self, password, salt):
        '''
        :type password: str
        :type salt: str
        '''
        
        return saltPassword(password, salt)

    def clear(self):
        self.clean_table(self.dbObject.tableName)
