'''
Created on Jan 2, 2016

@author: henrylevy
'''

class DatabaseObject(object):
    '''
        Base class for Database Objects
    '''
    TABLE_NAME = ""
    
    def __iter__(self):
        return []
    
    def asInsertStatement(self):
        data = dict(self)
        return """INSERT INTO {0} {1} VALUES {2};""".format(self.TABLE_NAME,
                                                              str(tuple(data.keys())).replace('\'', ''),
                                                              tuple(data.values()))