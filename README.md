SwipeLeftRightMenuListView
==========================

listView item slide to the left and right

 ![image](https://github.com/flyfei/SwipeLeftRightMenuListView/blob/master/gif/SwipeMenuLeftRightListViewSample.gif)
 
 
 Use:
 
 .xml
 
 \<com.fly.swipeleftrightmenulistview.SwipeLeftRightMenuListView
        android:layout_above="@id/btn"
        android:layout_width="match_parent"
        android:layout_height="match_parent" /\>
 
 
 .java
 
 // step 1. create a MenuCreator
		SwipeMenuCreator creatorLeft = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);

				// create "delete" item
				SwipeMenuItem a = new SwipeMenuItem(getApplicationContext());
				// set item background
				a.setBackground(new ColorDrawable(Color.rgb(0x00, 0x3F, 0x25)));
				// set item width
				a.setWidth(dp2px(90));
				// set a icon
				a.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(a);

				menu.setViewType(0);
			}
		};
		SwipeMenuCreator creatorRight = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				openItem.setId(1);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// // create "delete" item
				// SwipeMenuItem deleteItem = new
				// SwipeMenuItem(getApplicationContext());
				// // set item background
				// deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
				// 0x3F, 0x25)));
				// // set item width
				// deleteItem.setWidth(dp2px(90));
				// // set a icon
				// deleteItem.setIcon(R.drawable.ic_delete);
				// // add to menu
				// menu.addMenuItem(deleteItem);

				menu.setViewType(1);
			}
		};
		// set creator
		mListView.setLeftMenuCreator(creatorLeft);
		mListView.setRightMenuCreator(creatorRight);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeLeftRightMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {

				System.out.println("position:" + position + "  index:" + index + " menu.getViewType():" + menu.getViewType());

				switch (menu.getViewType()) {
				case 0:// zuo
					switch (index) {
					case 0:
						Toast.makeText(SimpleActivity.this, position + "行 左侧menu 0", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Toast.makeText(SimpleActivity.this, position + "行 左侧menu 1", Toast.LENGTH_SHORT).show();
						break;
					}
					break;
				case 1:// you

					switch (index) {
					case 0:
						Toast.makeText(SimpleActivity.this, position + "行 右侧menu 0", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Toast.makeText(SimpleActivity.this, position + "行 右侧menu 1", Toast.LENGTH_SHORT).show();
						break;
					}
					break;

				default:
					break;
				}

			}
		});
 
 
 
 Found a bug：
 
 If you find a bug, please send an email to zhaotengf, thank you
 
 
 
 
 
 
 
 
 
 
 Reference projects：
 
 https://github.com/daimajia/AndroidSwipeLayout
 
 
 
 
 
 
