target = "/mnt/sdcard/"+ARGV[1]+".txt"
system ("adb shell am profile start #{ARGV[0]} #{target}")
