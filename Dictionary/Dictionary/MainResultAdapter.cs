using System;
using Android.Widget;
using Android.App;
using System.Collections.Generic;
using Android.Views;
using Android.Util;

namespace Dictionary
{
	public class MainResultAdapter : BaseAdapter 
	{
		Activity Activity;
		List<SearchResult> Results;

		public MainResultAdapter( Activity a, List<SearchResult> r )
		{
			Log.Info ("WordListAdapter", "Initialized - results to show: " + r.Count);
			
			Activity = a;
			Results = r;
		}

		public override int Count {
			get { return Results.Count; }
		}

		public override Java.Lang.Object GetItem (int position) {
			// could wrap a Contact in a Java.Lang.Object
			// to return it here if needed
			return null;
		}

		public override long GetItemId (int position) {
			return Results [position].Id;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			var view = convertView ?? Activity.LayoutInflater.Inflate (
				Resource.Layout.ResultItem, parent, false);
			var dictionary = view.FindViewById<TextView> (Resource.Id.Dictionary);
			var definition = view.FindViewById<TextView> (Resource.Id.Definition);

			dictionary.Text = Results [position].Dictionary;
			definition.Text = Results [position].Definition;

			return view;
		}
	}
}

