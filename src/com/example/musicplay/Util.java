package com.example.musicplay;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
/**
 * 负责解析所有网站
 * @author zhang
 *
 */
public class Util {
	private static String SgCookie1, SgCookie2;
	   public static int  pageString=1;
	   public static String urlString="http://www.xiami.com/app/android/search-part?type=songs&page="+pageString+"&key=";
	static {
		try {
			URL reqURL2 = new URL("http://mp3.sogou.com/");
			HttpURLConnection urlConn = (HttpURLConnection) reqURL2
					.openConnection();
			urlConn.setRequestProperty("Accept",
					"text/html, application/xhtml+xml, */*");
			urlConn.setRequestProperty("Accept-Language", "zh-CN");
			urlConn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
			urlConn.setDoInput(true);

			for (int i = 0; i < 10; i++) {
				String cookieval = urlConn.getHeaderField(i);
				int httpStartPos;
				if (cookieval.startsWith("SSUID=")) {
					httpStartPos = cookieval.indexOf(";");
					if (httpStartPos > 0) {
						SgCookie1 = cookieval.substring(6, httpStartPos);
					}
				} else {
					if (cookieval.startsWith("SUV=")) {
						httpStartPos = cookieval.indexOf(";");
						if (httpStartPos > 0) {
							SgCookie2 = cookieval.substring(4, httpStartPos);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 虾米网 
	 * /**
 * 同样需要urf-8  来编码 
 * 这里可以解析 歌手图片 歌词 等
 * 搜索首选
 * @author zhang
 *
	 * getSRs("http://www.xiami.com/app/android/sear'" +
			"ch-part?type=songs&page=1&key=%E5%BF%83%E6%81%8B");
	 * @param reqURL
	 * @return
	 */
	public static LinkedList<MusicInfo> getXiamiSRs(String reqURL) {
	    LinkedList<MusicInfo> srs = new LinkedList<MusicInfo>();
		String msg = "";
		try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
				conn.setDoInput(true);
				InputStream in = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedInputStream(conn.getInputStream());
				}
				if (in != null) {
					byte[] buff = new byte[4 * 1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int b = -1;
					while ((b = in.read(buff)) != -1) {
						bos.write(buff, 0, b);
					}
					msg = new String(bos.toByteArray());
				}
				if (!"".equals(msg) && msg != null) {
					JSONArray jsonArray = new JSONObject(msg).getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {
						MusicInfo sr = new MusicInfo();
						String mArtist = "";
						String mSong = "";
						String mAlbum = "";
						String mSongURL = "";
						String mLyricURL = "";
						String singerimage = "";
						JSONObject jsonSong = (JSONObject) jsonArray.opt(i);
						mSong = jsonSong.getString("name");
						mArtist = jsonSong.getString("artist_name");
						mSongURL = jsonSong.getString("location");
						mAlbum = jsonSong.getString("album_name");// 这里解析出来的就是专辑名称，不是专辑的url
						singerimage = jsonSong.getString("logo");
						mLyricURL=jsonSong.getString("lyric");
						sr.specialString = mAlbum;
						sr.singerString = mArtist;
						sr.mLyricURL = mLyricURL;
						sr.nameString = mSong;
						sr.addresString = mSongURL;
						sr.singerIMage=singerimage;
						srs.add(sr);
					}
					in.close();
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return srs;

	}

	/**http://mp3.sogou.com/music.so?pf=mp3&ac=1&query=%D0%C4%C1%B5
	 * 同样为GBK 编码
	 * 同时需要二级请求地址
	 * 搜狗
	 */
	public static List<MusicInfo> getSougouSRs(String reqURL) {
		Object[] obj = null;
		List<MusicInfo> srs = new LinkedList<MusicInfo>();

				long starttime = System.currentTimeMillis();
				URL url;
				try {
					url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
				conn.setRequestProperty("Cookie", "SSUID=" + SgCookie1 + "; "
						+ "SUV=" + SgCookie2);
				conn.setDoInput(true);
				InputStream in = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedInputStream(conn.getInputStream());
				}
				long endtime = System.currentTimeMillis();

				long newtime = endtime - starttime;

				Document doc;
				Element songlist;
				Elements songs;
				Elements pagination = null;
				if (in != null) {
					doc = Jsoup.parse(in, "GBK", "网址");
					songlist = doc.select("#songlist").get(0);
					songs = songlist.select("tr");
					songs.remove(0);
					for (Element song : songs) {
						MusicInfo srb = new MusicInfo();
						Elements td = song.select("td");

						String mArtist = "";
						if (td.get(2).select("a[href]").size() > 0)
							mArtist = td.get(2).select("a[href]").get(0).text();

						String mSong = "";

						if (td.get(1).select("a[href]").size() > 0)
							mSong = td.get(1).select("a[href]").get(0).text();
						else
							continue;

						String mAlbum = "";
						if (td.get(3).select("a[href]").size() > 0)
							mAlbum = td.get(3).select("a[href]").get(0).text();
						String onclick = "";
						if (td.get(6).select("a[href]").size() > 0)
							onclick = td.get(6).select("a[href]").get(0)
									.attr("onclick");
						String mSongURL = "";
						if (onclick.length() > 0 && onclick.indexOf("'") != -1
								&& onclick.indexOf(",") != -1)
							mSongURL = "http://mp3.sogou.com"
									+ onclick.substring(onclick.indexOf("'"),
											onclick.indexOf(",")).replaceAll(
											"'", "");

						String mLyricURL = "http://mp3.sogou.com"
								+ td.get(7).select("a[href]").attr("href");
						String mAlbumURL = td.get(3).select("a[href]")
								.attr("href");
						srb.specialString = mAlbum;
						srb.specialURL = mAlbumURL;
						srb.singerString = mArtist;
						srb.mLyricURL = mLyricURL;//
						srb.nameString = mSong;
						srb.addresString = mSongURL;//
						srs.add(srb);
						System.out.println(mAlbum);
						System.out.println(mAlbumURL);			
					}
				in.close();
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		return srs;
	}
	/**
	 * 获取搜狗歌曲 下载地址
	 * 针对 搜狗搜索引擎
	 * @param firsturl
	 * @return
	 */
		public static String getSouGouSongLink(String firsturl) {

			URL url;
			String songurl = "";
			try {
				url = new URL(firsturl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
				conn.setRequestProperty("Cookie", "SSUID=" + SgCookie1 + "; "
						+ "SUV=" + SgCookie2);
				conn.setDoInput(true);
				InputStream in = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedInputStream(conn.getInputStream(), 4 * 1024);
				}
				Document doc = null;
				if (in != null) {
					doc = Jsoup.parse(in, "UTF-8", "网址");
					Element linkbox = doc.select(".linkbox").get(0);
					songurl = linkbox.select("a").get(0).attr("href");
					in.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return songurl;

		}
		/**
		 * 获取搜狗歌词 下载地址
		 * 针对 搜狗搜索引擎
		 * @param firsturl
		 * @return
		 */		
public static String getSouGouLrc(String lrcurl){
			URL url1;
			String songurl = "";
			try {
				url1 = new URL(lrcurl);
				HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
				conn.setRequestProperty("Cookie", "SSUID=" + SgCookie1 + "; "
						+ "SUV=" + SgCookie2);
				conn.setDoInput(true);
				InputStream in = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedInputStream(conn.getInputStream(), 4 * 1024);
				}
				Document doc = null;
				if (in != null) {
					doc = Jsoup.parse(in, "UTF-8", "网址");
					Elements linkbox = doc.getElementsByAttribute("href");
					for (Element element : linkbox) {
						songurl=element.attr("href");
						if (songurl.contains("downlrc")) {
							 System.out.println(songurl);
						}
		 
					}
					in.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return "http://mp3.sogou.com/"+songurl;

		}
/**
 * http://cgi.music.soso.com/fcgi-bin/m.q?w=%D0%C4%C1%B5&p=1
 * 汉语编码为 GBK
 * 嗖嗖
 * @author zhang
 *
 */
public static List<MusicInfo> getSosoSRs(String reqURL) {
	List<MusicInfo> srs = new LinkedList<MusicInfo>();
	try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
			conn.setDoInput(true);
			InputStream in = null;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = new BufferedInputStream(conn.getInputStream());
			}
			Document doc;
			Element song_box;
			Elements songs;
			if (in != null) {
				doc = Jsoup.parse(in, "GBK", "网址");
				song_box = doc.select(".song_box").get(0);
				songs = song_box.select("tr");
				songs.remove(0);
				for (Element song : songs) {
					MusicInfo srb = new MusicInfo();
					Elements td = song.select("td");
					String mArtist = "";
					if (td.get(2).select("a[href]").size() > 0)
						mArtist = td.get(2).select("a[href]").get(0).text();
					String mSong = "";
					if (td.get(1).select("a[href]").size() > 0)
						mSong = td.get(1).select("a[href]").get(0).text();
					else
						continue;
					String mAlbum = "";
					if (td.get(3).select("a[href]").size() > 0)
						mAlbum = td.get(3).select("a[href]").get(0).text();
					String mSongURL = "";
					if (td.get(0).text().indexOf("http:") != -1
							&& td.get(0).text().indexOf(";") != -1) {
						String s = td	.get(0).text().substring(td.get(0).text().indexOf("http:"));
						mSongURL = s.substring(s.indexOf("http:"),
								s.indexOf(";"));
					}
					String mLyricURL = "http://mp3.sogou.com"
							+ td.get(7).select("a[href]").attr("href");
					String mAlbumURL = td.get(3).select("a[href]")
							.attr("href");
					srb.specialString = mAlbum;
					srb.specialURL = mAlbumURL;
					srb.singerString = mArtist;
					srb.mLyricURL = mLyricURL;
					srb.nameString = mSong;
					srb.addresString = mSongURL;
					srs.add(srb);
				/*	http://stream6.qqmusic.qq.com/13539449.wma
						安桥试音碟3 A2HD
						http://music.soso.com/portal/albumn/45/albumn_3776043645.html
						http://mp3.sogou.com
						心恋
						4.4M*/

	
					// in.close();
			/*		安桥试音碟3 A2HD//
					http://stream6.qqmusic.qq.com/13539449.wma
					http://mp3.sogou.com
					http://music.soso.com/portal/albumn/45/albumn_3776043645.html
*/					}

				in.close();

		

			}
		

	} catch (MalformedURLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	return srs;
}

/**
 *         获取mp3Bear歌曲
 * @param reqURL http://mp3bear.com/?q=next（歌名）
 * * @return   一级地址 与歌名
 */
	public static List<MusicInfo> getBearSRs(String reqURL) {
		List<MusicInfo> srs = new LinkedList<MusicInfo>();
		URL url;
		try {
				url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET"); 
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
				conn.setDoInput(true);
				InputStream in = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = conn.getInputStream();
				Document doc = null;
				Element table = null;
				Elements trs = null;
				if (in != null) {
					doc = Jsoup.parse(in, "UTF-8", "网址");
					table = doc.select("table").get(0);
					trs = table.select("tr");

					trs.remove(0);
					for (Element tr : trs) {
						MusicInfo sr = new MusicInfo();
						String mSong = "";
						if (tr.select("a[href]").size() > 0)
							mSong = tr.select("a[href]").get(0).text();
						else
							continue;
						String mSongURL = "";
						if (tr.select("a[href]").size() > 0)
							mSongURL = tr.select("a[href]").get(0).attr("href");
						sr.nameString = mSong;
						sr.addresString = mSongURL;
						System.out.println("地址"+mSongURL);
						srs.add(sr);
					}}
					in.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return srs;
	}

	/**
	 * 获取音乐真是地址   针对 mp3Bear 网
	 * @param reqURL http://mp3bear.com/（由地址解析出来）tempa-t-next-hype-acapella
	 * @return 
	 */
	public static String getMP3BearSongLink(String firsturl) {
		String songurl = "";
		try {
			URL requrl = new URL(firsturl);
			HttpURLConnection conn = (HttpURLConnection) requrl
					.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
			conn.setDoInput(true);
			InputStream in = null;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = new BufferedInputStream(conn.getInputStream(), 4 * 1024);
			}
			Document doc = null;
			Elements tabs = null;
			Element tab = null;
			if (in != null) {
				doc = Jsoup.parse(in, "UTF-8", "网站");
				tabs = doc.select("div#tabmenu");
				tab = tabs.get(0).select("div#t1").get(0).select("a[href]")
						.get(0);
				String onclickcontent = tab.attr("onclick");
				String id = onclickcontent.substring(
						onclickcontent.lastIndexOf(",") + 2,
						onclickcontent.lastIndexOf(")") - 1);
				String head = onclickcontent.substring(
						onclickcontent.indexOf("(") + 2,
						onclickcontent.indexOf(",") - 1);
				songurl = "http://mp3bear.com/inc/audio.php?id=" + head + "_"
						+ id;
				System.out.println(songurl);
				in.close();
				conn.disconnect();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return songurl;
	}
	/**
	 * 获取搜索结果
	 * /**
	 *  网站  http://en.dilandau.eu/download-songs-mp3/because of you /1.html
	 * @author zhang
	 *
	 * @param reqURL
	 * @return
	 */
		public static List<MusicInfo> getDIandauSRs(String reqURL) {
			List<MusicInfo> srs = new LinkedList<MusicInfo>();
			URL url;
			try {
					url = new URL(reqURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET"); 
					conn.setRequestProperty(  
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
					conn.setDoInput(true);
					InputStream in = null;
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						in = conn.getInputStream();
				Document doc = Jsoup.parse(in, "UTF-8", "d");
				Elements ur =  	doc.getElementsByAttribute("onclick");
					Elements es = doc.select("h4");
					
					for (Element e : es) {
						MusicInfo musicInfo=new MusicInfo();
						musicInfo.nameString=e.text();
						srs.add(musicInfo);
					}
					srs.remove(0);
					srs.remove(srs.size()-1);
				for (int i = 0; i < ur.size(); i++) {
				String exe=	ur.attr("onclick");
					srs.get(i).addresString=exe.substring(exe.indexOf("'"),exe.lastIndexOf("'")).replace("+", "").replace("'","").replace(" ","");
				}
					}
				in.close();

				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return srs;
		}
		/**
		 *获取duandian的歌手 信息
		 * @param reqURL
		 * @return
	
		public static LinkedList<SingerBean> getDiandanSInger(String reqURL) {
			// src   data-original
			//http://en.dilandau.eu/top/P!nk/download-songs-for-free.html
			
			LinkedList<SingerBean> srs = new LinkedList<SingerBean>();
			LinkedList<SingerBean> srs2= new LinkedList<SingerBean>();
			URL url;
			try {
					url = new URL(reqURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET"); 
					conn.setRequestProperty(  
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
					conn.setDoInput(true);
					InputStream in = null;
					
					if (conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
						in = new BufferedInputStream(conn.getInputStream());
						Document doc=Jsoup.parse(in,"utf-8","x");
						Elements image=doc.select("img[data-original]");
						for (int i = 0; i < image.size()/6; i++) {
							SingerBean singerBean=new SingerBean();
							singerBean.imageString=image.get(i).attr("data-original");
							srs.add(singerBean);
						}
						System.out.println();
						Elements name=doc.getElementsByClass("track-artist");
						for (int i = 0; i < name.size()/6; i++) {
							SingerBean nasingerBean=new SingerBean();
							Element element=name.get(i);
							nasingerBean.nameString=element.text();
							nasingerBean.linkString=element .attr("href");
							srs2.add(nasingerBean);
						}
						for (int i = 0; i < srs.size(); i++) {
							if (i<srs.size()&&i<srs2.size()) {
								srs2.get(i).imageString=srs.get(i).imageString;
							}
						}

								}
					
			}		catch (Exception e) {
				e.printStackTrace();
				
					}
			return srs2;
}	   */
	public static String getEncodeUrl(String url) {
			String tmpUrlString = "";
			String tmpChineseUrl = "";
			boolean isMusicEncode = false;
			for (int i = 0; i < url.length(); i++) {
				char tmpchar = url.charAt(i);
				if (isChinese(tmpchar)) {
					isMusicEncode = true;
					tmpChineseUrl = tmpChineseUrl + tmpchar;
				} else {
					if (isMusicEncode) {
						if (tmpchar == 32)
							tmpUrlString = tmpUrlString
									+ URLEncoder.encode(tmpChineseUrl) + "%20";
						else {
							tmpUrlString = tmpUrlString
									+ URLEncoder.encode(tmpChineseUrl) + tmpchar;
						}
						tmpChineseUrl = "";
						isMusicEncode = false;
					} else if (tmpchar == 32)// 空格
						tmpUrlString = tmpUrlString + "%20";
					else
						tmpUrlString = tmpUrlString + tmpchar;
				}
			}
			return tmpUrlString;
		}
		private static boolean isChinese(char tmpchar) {
			if ((tmpchar >= 0x4e00) && (tmpchar <= 0x9fbb)) {
				return true;
			} else {
				return false;
			}
		}
		/**
		 * 未完成的
		 *  mp3 world
		http://www.emp3world.com/search/next_mp3_download.html
			 * @author zhang
			 *http://www.emp3world.com/search/next_mp3_download.html
			 param url
		 * @return
		 */
		public static List<MusicInfo> getMp3WorldSRs(String reqURL) {
			List<MusicInfo> srs = new LinkedList<MusicInfo>();
			try {
					URL url = new URL(reqURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6");
					conn.setDoInput(true);
					InputStream in = null;
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						in = new BufferedInputStream(conn.getInputStream());
					}
					Document doc;
					Elements song_box;
					Elements songs;
					if (in != null) {
						doc = Jsoup.parse(in, "UTF-8", "网址");
						songs=doc.getElementsByAttribute("value");
						for (int i = 0; i < songs.size()/4; i++) {//少解析点 这个太多了
							MusicInfo srb=new MusicInfo();
							String addString=songs.get(i).attr("value");
							if (addString.contains(".mp3")&&addString.contains("http:")) {
								srb.addresString=addString;
						        srs.add(srb);
							}
							song_box=doc.getElementsByAttributeValue("id","song_title");
						        for (Element element : song_box) {
						        	srb.nameString=element.text();
					            	}
						
						        
						/*	srb.specialString = ;
							srb.nameString = mSong;
							srb.addresString = mSongURL;
							srs.add(srb);
							System.out.println(mLyricURL);
							System.out.println(mSongURL);*/
						
						}
						
						for (int i = 0; i < srs.size(); i++) {
							System.out.println(srs.get(i).addresString);
						}
						in.close();

				

					}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return srs;
		}

        public static void downland(DownloadManager downloadManager,String url,String name ) {
		    Uri uri = Uri.parse(url);
		    DownloadManager.Request request = new Request(uri);
		    request.allowScanningByMediaScanner();//允许扫面
		    //设置允许使用的网络类型，这里是移动网络和wifi都可以  
		    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);  
		    //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION  
		//     request.setNotificationVisibility(View.VISIBLE);  
		    request.setVisibleInDownloadsUi(false);
//		      设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，
		    //下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，
		    //如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面
		//request.setDestinationInExternalFilesDir(this, null, "tar.apk");
		    File path = Environment.getExternalStoragePublicDirectory(//检查 music 公共目录是否存在
		             Environment.DIRECTORY_MUSIC);
		    if (!path.isDirectory()) {
		        path.mkdirs();
		    }
		    try {
                
            } catch (Exception e) {
                // TODO: handle exception
            }
		    try {
	                  String uString=  url.substring(url.lastIndexOf("."));
		    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,name +uString);
		    } catch (Exception e) {
		        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,name +".mp3");//设置下载到music 目录里面
            }
		
	      	long id = downloadManager.enqueue(request);
		}
}
