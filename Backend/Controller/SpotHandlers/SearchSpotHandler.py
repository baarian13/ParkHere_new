'''
Created on Oct 28, 2016

@author: henrylevy
'''

import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json

class SearchSpotHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        print self.get_argument("address")
        results = [{'address'  : res[0],
                    'start'    : str(res[1]),
                    'end'      : str(res[2]),
                    'distance' : res[3]}
            for res in self.db.searchForSpots(self.get_argument("address"))]
        self.write(json.dumps(results))