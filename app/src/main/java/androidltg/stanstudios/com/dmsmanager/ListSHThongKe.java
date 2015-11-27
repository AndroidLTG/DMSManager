//package androidltg.stanstudios.com.dmsmanager;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
///**
// * Created by DMSv4 on 9/3/2015.
// */
//public class ListSHThongKe extends ArrayAdapter<SalesHeader> {
//    Activity context = null;
//    ArrayList<SalesHeader> myArray = null;
//    int layoutId;
//
//    /**
//     * Constructor này dùng để khởi tạo các giá trị
//     * từ MainActivity truyền vào
//     *
//     * @param context   : là Activity từ Main
//     * @param layoutId: Là layout custom do ta tạo (my_item_layout.xml)
//     * @param arr       : Danh sách nhân viên truyền từ Main
//     */
//    public ListSHThongKe(Activity context,
//                                 int layoutId,
//                                 ArrayList<SalesHeader> arr) {
//        super(context, layoutId, arr);
//        this.context = context;
//        this.layoutId = layoutId;
//        this.myArray = arr;
//    }
//
//    /**
//     * hàm dùng để custom layout, ta phải override lại hàm này
//     * từ MainActivity truyền vào
//     *
//     * @param position     : là vị trí của phần tử trong danh sách nhân viên
//     * @param convertView: convertView, dùng nó để xử lý Item
//     * @param parent       : Danh sách nhân viên truyền từ Main
//     * @return View: trả về chính convertView
//     */
//    public View getView(int position, View convertView,
//                        ViewGroup parent) {
//
//        LayoutInflater inflater =
//                context.getLayoutInflater();
//        convertView = inflater.inflate(layoutId, null);
//        //chỉ là test thôi, bạn có thể bỏ If đi
//        if (myArray.size() > 0 && position >= 0) {
//            //dòng lệnh lấy TextView ra để hiển thị Mã và tên lên
//            final TextView txtlvname = (TextView)
//                    convertView.findViewById(R.id.txtlvnameitem);
//            //lấy ra nhân viên thứ position
//            final SalesHeader emp = myArray.get(position);
//            //đưa thông tin lên TextView
//            //emp.toString() sẽ trả về Id và Name
//            txtlvname.setText(emp.getName());
//            //lấy ImageView ra để thiết lập hình ảnh cho đúng
//            final TextView txtlvquantity = (TextView)
//                    convertView.findViewById(R.id.txtlvquantity);
//            txtlvquantity.setText(emp.getquantity());
//            final TextView txtlvdescription = (TextView)
//                    convertView.findViewById(R.id.txtlvdescription);
//            txtlvdescription.setText(emp.getdescription());
//            final TextView txtlvprices = (TextView)
//                    convertView.findViewById(R.id.txtlvprice);
//            txtlvprices.setText(emp.getprices());
//            final TextView txtgiamgiatheomon = (TextView)
//                    convertView.findViewById(R.id.txtgiamtheomon);
//            if (emp.getDiscount() > 0)
//                txtgiamgiatheomon.setText("- " + MyGlobal.formatfloat(emp.getDiscount()));
//            else if (emp.getDiscountPT() > 0)
//                txtgiamgiatheomon.setText("- " + MyGlobal.formatfloat(emp.getDiscountPT())+"%");
//            else txtgiamgiatheomon.setText("");
//        }
//        return convertView;
//    }
//}
