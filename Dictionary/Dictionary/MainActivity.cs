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

namespace Dictionary
{
	[Activity (Label = "Dinky Dictionary", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{

		EditText SearchField;
		ImageButton SearchButton, FilterButton, ShareButton;
		TextView SearchInfo;
		ListView WordList;

		MainResultAdapter Adapter;

		WebClient Client;
		Uri Uri;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			Log.Info ("Dictionary", "Hello World!");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			Client = new WebClient();
			Uri = new Uri ("http://services.aonaware.com/DictService/DictService.asmx/Define");

			SearchField = FindViewById<EditText> (Resource.Id.SearchField);
			SearchButton = FindViewById<ImageButton> (Resource.Id.SearchButton);
			FilterButton = FindViewById<ImageButton> (Resource.Id.FilterButton);
			ShareButton = FindViewById<ImageButton> (Resource.Id.ShareButton);
			SearchInfo = FindViewById<TextView> (Resource.Id.SearchInfo);
			WordList = FindViewById<ListView> (Resource.Id.WordList);

			SearchButton.Enabled = ShareButton.Enabled = false;

			SearchField.AfterTextChanged += (sender, e) => SearchHandler();
			SearchButton.Click += (sender, e) => SearchHandler();
			FilterButton.Click += (sender, e) => FilterHandler();
			ShareButton.Click += (sender, e) => ShareHandler();
		}

		private void PopulateWordList( object sender, UploadValuesCompletedEventArgs e){
			String SearchQuery = SearchField.Text.Trim ();

			if (SearchQuery != "") {
				if (e.Error == null) {
					 
					List<WordResult> Results = new List<WordResult> ();
					XmlDocument Response = new XmlDocument ();

					Response.Load (new MemoryStream (e.Result));
				
					XmlNodeList Definitions = Response.GetElementsByTagName ("Definition");

					SearchInfo.Text = Definitions.Count + " definitions in X dictionaries for “" + SearchQuery + "”.";
					if(Definitions.Count == 1 )
						SearchInfo.Text = Definitions.Count + " definition in 1 dictionary for “" + SearchQuery + "”.";

					foreach (XmlNode d in Definitions) {
						//String dictionary = definition["Dictionary"]["Name"].InnerText;
						//String worddefinition = definition["WordDefinition"].InnerText;
						//Console.WriteLine ("Element: " + dictionary + " " + worddefinition);
						Results.Add (new WordResult {
							Id = Results.Count,
							DictId = d ["Dictionary"] ["Id"].InnerText,
							Dictionary = d ["Dictionary"] ["Name"].InnerText,
							Definition = d ["WordDefinition"].InnerText
						});
					}

					Adapter = new MainResultAdapter (this, Results);
					WordList.Adapter = Adapter;

				} else {
					SearchInfo.Text = "Looking for definitions...";
				}
			} else {
				SearchInfo.Text = "";
			}
		}

		private void SearchHandler(){
			Log.Info ("Dictionary", "SearchHandler()");

			SearchButton.Enabled = ShareButton.Enabled = ( SearchField.Text.Trim() == "" ) ? false : true;

			NameValueCollection Parameters = new NameValueCollection();

			Parameters.Add ( "word", SearchField.Text.Trim() );

			Client.UploadValuesCompleted += PopulateWordList;
			Client.CancelAsync ();
			try{
				Client.UploadValuesAsync (Uri, Parameters);
			}catch(Exception e){
				// fuck the police
				Log.Info ("MainActivity", "UploadValuesAsync() -> EXCEPTION :(" );
			}
		}

		private void FilterHandler(){
			Log.Info ("Dictionary", "FilterHandler()");

			var intent = new Intent(this, typeof(DictionaryPickerActivity));
			//intent.PutStringArrayListExtra("phone_numbers", phoneNumbers);
			StartActivity(intent);
		}

		private void ShareHandler(){
			Log.Info ("Dictionary", "ShareHandler()");
		}
	
	}

	public class WordResult{
		public long Id{ get; set;}
		public String DictId{ get; set; }
		public String Dictionary{ get; set; }
		public String Definition{ get; set; }
	}
}