'''
Created on Jan 2, 2016

@author: henrylevy
'''

class DatabaseObject(object):
    '''
        Base class for Database Objects
    '''
    TABLE_NAME = ""
    
    def __init__(self):
        self.data = {}
    
    def asInsertStatement(self):
        return """INSERT INTO {0} {1} VALUES ({2});""".format(self.TABLE_NAME, self.data.keys(), self.data.values())