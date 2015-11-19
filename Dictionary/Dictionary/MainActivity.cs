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

using Dictionary.services.aonaware.com;

namespace Dictionary
{
	[Activity (Label = "Dinky Dictionary", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity 
	{
		EditText searchField;
		ImageButton searchButton, filterButton, shareButton;
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

			searchField = FindViewById<EditText> (Resource.Id.SearchField);
			searchButton = FindViewById<ImageButton> (Resource.Id.SearchButton);
			filterButton = FindViewById<ImageButton> (Resource.Id.FilterButton);
			shareButton = FindViewById<ImageButton> (Resource.Id.ShareButton);
			searchInfo = FindViewById<TextView> (Resource.Id.SearchInfo);
			wordList = FindViewById<ListView> (Resource.Id.WordList);

			searchButton.Enabled = shareButton.Enabled = false;

			searchField.AfterTextChanged += (sender, e) => SearchHandler();
			searchButton.Click += (sender, e) => SearchHandler();
			filterButton.Click += (sender, e) => FilterHandler();
			shareButton.Click += (sender, e) => ShareHandler();

			wordResultList = new List<WordResult> ();
			adapter = new MainResultAdapter (this, wordResultList);
			wordList.Adapter = adapter;
		}

		private void PopulateWordList( String query ){

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
						searchInfo.Text = "Found 1 definition in 1 dictionary";
					else if( wordResultList.Count == 0 )
						searchInfo.Text = "Found no definitions";
					else
						searchInfo.Text = "Found " + wordResultList.Count + " definitions in X dictionaries";

					adapter.NotifyDataSetChanged();
				}, null);
		
			} else {
				searchInfo.Text = "";
			}
		}

		private void SearchHandler(){
			Log.Info ("Dictionary", "SearchHandler()");

			searchButton.Enabled = shareButton.Enabled = ( searchField.Text.Trim() == "" ) ? false : true;
			searchInfo.Text = "Looking for definitions...";

			PopulateWordList (searchField.Text.Trim());
		}

		private void FilterHandler(){
			Log.Info ("Dictionary", "FilterHandler()");

			var intent = new Intent(this, typeof(DictionaryPickerActivity));
			//intent.PutStringArrayListExtra("dict", dict);
			StartActivity(intent);
		}

		private void ShareHandler(){
			Log.Info ("Dictionary", "ShareHandler()");
			//TODO
		}
	
	}

	public class WordResult{
		public long Id{ get; set;}
		public String DictId{ get; set; }
		public String Dictionary{ get; set; }
		public String Definition{ get; set; }
	}
}