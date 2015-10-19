using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Util;

namespace Chronometer
{
	[Activity (Label = "Chronometer", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{
		
		TextView timeView;
		Button startStopButton;
		Button resetButton;
		int count = 0;
		ISharedPreferences data;
		ISharedPreferencesEditor editor;

		protected override void OnCreate (Bundle bundle)
		{

			base.OnCreate (bundle);
			Log.Info ("Chronometer", "Hello World");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);
			data = Application.Context.GetSharedPreferences("Data", FileCreationMode.Private);
			editor = data.Edit ();

			// Get our button from the layout resource,
			// and attach an event to it
			timeView = FindViewById<TextView> (Resource.Id.timeView);
			startStopButton = FindViewById<Button> (Resource.Id.startStopButton);
			resetButton = FindViewById<Button> (Resource.Id.resetButton);
			
			startStopButton.Click += (sender, e) => StartStopHandler();
			resetButton.Click += (sender, e) => StartStopHandler();
		}

		private void ShowCount(){
			timeView.Text = count.ToString();
		}

		private void Increment(){
			count++;
		}

		private void StartStopHandler(){
			Log.Info ("Chronometer", "startStopHandler()");
			Console.WriteLine (timeView);
			Increment ();
			ShowCount ();
		}
		/*
		protected override void OnSaveInstanceState(Bundle outState) {
			base.OnSaveInstanceState (outState);
			outState.PutInt ("count", count );

		}

		protected override void OnRestoreInstanceState (Bundle savedInstanceState) {
			base.OnRestoreInstanceState (savedInstanceState);
			count = (int) savedInstanceState.Get ("count");
			ShowCount ();
		}*/

		protected override void OnResume(){
			base.OnResume ();
			count = data.GetInt ("Count", 0 );
			ShowCount ();
		}
			
		protected override void OnPause(){
			base.OnPause ();
			editor.PutInt ("Count", count );
			editor.Apply ();

			Log.Info ("yolo", count.ToString() );
			Log.Info ( "swag", data.GetInt ("Count", 69).ToString() );
		}
	}
}


