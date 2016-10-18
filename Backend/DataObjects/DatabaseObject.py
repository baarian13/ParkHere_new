'''
Created on Jan 2, 2016

@author: henrylevy
'''

class DatabaseObject(object):
    '''
        Base class for Database Objects
    '''
    TABLE_NAME = ""
    
    def asInsertStatement(self):
        raise NotImplementedError()