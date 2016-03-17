local = "L:/EHBResult/"
sdcard = "/mnt/sdcard/"
localfile = local+ARGV[1]+".txt"
sdcardfile = sdcard+ARGV[1]+".txt"
system("adb shell am profile stop #{ARGV[0]}")
sleep(4)

system("adb pull #{sdcardfile} #{localfile}")