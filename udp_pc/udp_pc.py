import socket
import threading
import json

#user_id = 2

PORT = 5555
BUF_SIZE = 8192

def listen():
    s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
    max_ver = 3
    s.bind(("",PORT))
    print 'waiting on port:',PORT
    while True:
        data,addr = s.recvfrom(BUF_SIZE)
        print 'recived:',data
        print "address", addr

        info_file = open("info.json",'r')
        info = json.load(info_file)
        if data[0:7] == "user_id":
            user_id = data[8:]
            user_id = int(user_id)
            try:
                for item in info["ADDRESS"]:
                    if item["user_id"] == user_id:
                        if item["ip"] != addr[0] or item["port"] != addr[1]:
                            max_ver += 1
                            item["ip"] = addr[0]
                            item["port"] = addr[1]
                            item["version"] = max_ver
                            break
            except:
                max_ver += 1
                temp = {"ip":addr[0],"port":addr[1],"user_id":user_id,"version":max_ver}
                info["ADDRESS"].append(temp)
        else:
            temp = eval(data)
            info["ANNOTATION"] = temp["ANNOTATION"]


        f = open("info.json","w")
        json.dump(info,f)
        f.close()
        info_file.close()

def send():
    print "omg"
    f = open("info.json")
    info = f.read()
    mobile = raw_input("Please enter IP address:")
    port = input("Please Enter Port#:")
    s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
    s.sendto("user_id 2",(mobile,port))
    s.sendto(info,(mobile,port))
    print "gmo"


def main():
    t1 = threading.Thread(target=listen)
    t2 = threading.Thread(target=send)
    t1.start()
    t2.start()
    t1.join()
    t2.join()

if __name__ == '__main__':
    main()
