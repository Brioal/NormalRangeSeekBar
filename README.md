# NormalRangeSeekBar
范围选择控件
####效果演示:
![](https://github.com/Brioal/NormalRangeSeekBar/blob/master/art/1.gif)
####添加方法
####Step 1. Add the JitPack repository to your build file
####Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
####Step 2. Add the dependency
```
	dependencies {
	        compile 'com.github.Brioal:NormalRangeSeekBar:1.0'
	}
```
####使用示例
```
mRangeBar = (RangeBar) findViewById(R.id.main_rengae);
        mRangeBar.setBeginValue(20);
        mRangeBar.setFinishValue(50);
        mRangeBar.setIndex("%");
        mRangeBar.setInitValue(23, 40);
        mRangeBar.setRangeChangeListener(new OnRangeChangedListener() {
            @Override
            public void selected(int start, int end) {
                Log.i("Range:", start + ":" + end);
            }
        });
```
