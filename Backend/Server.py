'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado
from tornado import netutil, process, httpserver
from Controller.AbstractBaseHandler import MainHandler
from Controller.UserHandlers.SinginHandler import SigninHandler
from tornado.ioloop import IOLoop
from Controller.UserHandlers.SingupHandler import SignUpHandler
settings = {
    "cookie_secret": "ADSFGHARY3457fgSDFHSDFjusdfASDFGH2345h=sdg",
    "login_url": "/signin",
}
application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/signin", SigninHandler),
    (r"/signup", SignUpHandler),
], **settings)

def main():
    sockets = netutil.bind_sockets(8888)
    process.fork_processes(0)
    server = httpserver.HTTPServer(application)
    server.add_sockets(sockets)
    IOLoop.current().start()

if __name__ == "__main__":
    main()