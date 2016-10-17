'''
Created on Jan 2, 2016

@author: henrylevy
'''

class DatabaseObject(object):
    '''
        Base class for Database Objects
    '''
    DB_INDEX_FIELDS = {}
    
    PRIMARY_KEY_NAME = 'ID'
    
    PRIMARY_KEY = None
    
    DB_FIELDS = {}
    
    def table_creation_string(self, *args, **kwargs):
        return ''