using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using System.Net;
using System.Collections.Specialized;
using System.Linq;
using System.IO;
using System.Xml;
using System.Collections.Generic;
using Android.Util;
using Android.Support.V7.App;
using SupportToolbar = Android.Support.V7.Widget.Toolbar;
using Android.Support.Design;

using Dictionary.services.aonaware.com;

namespace Dictionary
{
	[Activity (Label = "Dinky Dictionary", MainLauncher = true, Icon = "@drawable/icon", Theme="@style/Dinky")]
	public class MainActivity : ActionBarActivity 
	{
		SupportToolbar toolbar;

		EditText searchField;
		//ImageButton searchButton;
		TextView searchInfo;
		ListView wordList;

		MainResultAdapter adapter;
		List<WordResult> wordResultList;

		DictService service;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			Log.Info ("Dictionary", "Hello World!");

			service = new DictService ();

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			toolbar = FindViewById<SupportToolbar> (Resource.Id.toolbar);
			searchField = FindViewById<EditText> (Resource.Id.SearchField);
			//searchButton = FindViewById<ImageButton> (Resource.Id.SearchButton);
			searchInfo = FindViewById<TextView> (Resource.Id.SearchInfo);
			wordList = FindViewById<ListView> (Resource.Id.WordList);

			SetSupportActionBar (toolbar);
			SupportActionBar.Title = "Dinky Dictionary";

			//searchButton.Enabled = false;

			searchField.AfterTextChanged += (sender, e) => SearchHandler();
			//searchButton.Click += (sender, e) => SearchHandler();

			wordResultList = new List<WordResult> ();
			adapter = new MainResultAdapter (this, wordResultList);
			wordList.Adapter = adapter;
		}

		public override bool OnCreateOptionsMenu (IMenu menu)
		{
			MenuInflater.Inflate (Resource.Menu.ActionMenu, menu);

			toolbar.MenuItemClick += MenuHandler;

			return base.OnCreateOptionsMenu (menu);
		}

		private void PopulateWordList( String query ){

			wordResultList.Clear();

			if (query != "") {
				service.BeginDefine(query, (ar) => {
					WordDefinition wd = service.EndDefine(ar);
					//WordDefinition wd = service.Define(query);
					
					foreach (Definition d in wd.Definitions) {
						wordResultList.Add (new WordResult {
							Id = wordResultList.Count,
							DictId = d.Dictionary.Id,
							Dictionary = d.Dictionary.Name,
							Definition = d.WordDefinition
						});
					}

					if( wordResultList.Count == 1 )
						searchInfo.Text = "Found 1 definition in 1 dictionary for “" + query +"”";
					else if( wordResultList.Count == 0 )
						searchInfo.Text = "Found no definitions for “" + query +"”";
					else
						searchInfo.Text = "Found " + wordResultList.Count + " definitions in X dictionaries for “" + query +"”";
					
					//adapter.NotifyDataSetChanged();
				}, null);
		
			} else {
				searchInfo.Text = " ";
			}
		}

		private void SearchHandler(){
			Log.Info ("Dictionary", "SearchHandler()");

			//searchButton.Enabled = ( searchField.Text.Trim() == "" ) ? false : true;
			searchInfo.Text = "Looking for definitions...";

			PopulateWordList (searchField.Text.Trim());
		}

		private void FilterHandler(){
			Log.Info ("Dictionary", "FilterHandler()");

			var intent = new Intent(this, typeof(DictionaryPickerActivity));
			//intent.PutStringArrayListExtra("dict", dict);
			StartActivity(intent);
		}

		private void MenuHandler( object sender, SupportToolbar.MenuItemClickEventArgs e){
			switch (e.Item.ItemId) {
			case Resource.Id.action_choose:
				Log.Info ("Dictionary", "Choose");
				//TODO
				break;
			case Resource.Id.action_share:
				Log.Info ("Dictionary", "Share");

				Intent intent = new Intent( Intent.ActionSend ); 
				intent.SetType("text/plain");

				String shareBody = "Yolo Swag Wafa";

				intent.PutExtra( Intent.ExtraSubject, "A definition by Dinky Dictionary");
				intent.PutExtra( Intent.ExtraText, shareBody);

				StartActivity(Intent.CreateChooser(intent, "Share via"));
				break;
			}
		}
	
	}

	public class WordResult{
		public long Id{ get; set;}
		public String DictId{ get; set; }
		public String Dictionary{ get; set; }
		public String Definition{ get; set; }
	}
}