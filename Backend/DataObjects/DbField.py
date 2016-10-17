'''
Created on Mar 25, 2016

@author: henrylevy
'''

class DBField(object):
    
    def __init__(self, name, sqlType, preNameModifier=None,
                 preTypeModifier=None, postTypeModifier=None):
        self.name = name
        self.sqlType = sqlType
        self.preNameModifier = preNameModifier
        self.preTypeModifier = preTypeModifier
        self.postTypeModifier = postTypeModifier
        
    def __str__(self, *args, **kwargs):
        return ''