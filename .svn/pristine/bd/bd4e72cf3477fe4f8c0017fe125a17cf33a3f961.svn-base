from com.android.monkeyrunner import MonkeyRunner,MonkeyDevice
from com.android.monkeyrunner.easy import EasyMonkeyDevice
from com.android.monkeyrunner.easy import By

s = 'OpenSudoku'
views = [('cz.romario.opensudoku/cz.romario.opensudoku.gui.FolderListActivity','get_more_puzzles')]
#view = 'get_more_puzzles'
device = MonkeyRunner.waitForConnection()     
easy_device = EasyMonkeyDevice(device)

for view in views:
	sourceActivity = view[0]
	v = view[1]
	easy_device.startActivity(sourceActivity)
	MonkeyRunner.sleep(3.0)     
	result1 = device.takeSnapshot()
	result1.writeToFile('./'+v+'1.png','png')
	easy_device.touch(By.id('id/'+v),MonkeyDevice.DOWN_AND_UP)
	MonkeyRunner.sleep(3.0)
	result = device.takeSnapshot()
	result.writeToFile('./'+v+'2.png','png')
