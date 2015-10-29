using System;

using System.Collections.Generic;
using Android.App;
using Android.OS;
using Android.Widget;
using System.Net;
using System.Collections.Specialized;
using Android.Util;
using System.Xml;
using System.IO;

namespace Dictionary
{
	[Activity (Label = "Dictionary Picker")]			
	public class DictionaryPickerActivity : Activity
	{
		ListView DictionaryList;

		WebClient Client;
		Uri Uri;

		//DictionaryListAdapter Adapter;
		
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			Log.Info ("Dictionary", "DictionaryPickerActivity");

			Client = new WebClient();
			Uri = new Uri ("http://services.aonaware.com/DictService/DictService.asmx/Define");

			DictionaryList = FindViewById<ListView> (Resource.Id.DictionaryList);

			LoadDictionaries ();
		}

		private void PopulateWordList( object sender, UploadValuesCompletedEventArgs e)
		{
			List<DictionaryResult> Results = new List<DictionaryResult> ();
			XmlDocument Response = new XmlDocument ();

			Response.Load (new MemoryStream (e.Result));

			XmlNodeList Dictionaries = Response.GetElementsByTagName ("Dictionary");

			foreach (XmlNode d in Dictionaries) {
				//String dictionary = definition["Dictionary"]["Name"].InnerText;
				//String worddefinition = definition["WordDefinition"].InnerText;
				//Console.WriteLine ("Element: " + dictionary + " " + worddefinition);
				Results.Add (new DictionaryResult {
					Id = Results.Count,
					Dictionary = d ["Dictionary"] ["Name"].InnerText,
				    Enabled = true
				});
			}

			Adapter = new MainResultAdapter (this, Results);
			WordList.Adapter = Adapter;
		}

		private void LoadDictionaries(){
			Log.Info ("Dictionary", "LoadDictionaries()");

			Client.UploadValuesCompleted += PopulateWordList;
			Client.CancelAsync ();
			try{
				Client.UploadValuesAsync (Uri, null);
			}catch(Exception e){
				// fuck the police
				Log.Info ("DictionaryPickerActivity", "UploadValuesAsync() -> EXCEPTION :(" );
			}
		}
	}

	public class DictionaryResult{
		public long Id{ get; set; }
		public String DictId{ get; set; }
		public String Name{ get; set; }
		public bool Enabled{ get; set; }
	}
}

