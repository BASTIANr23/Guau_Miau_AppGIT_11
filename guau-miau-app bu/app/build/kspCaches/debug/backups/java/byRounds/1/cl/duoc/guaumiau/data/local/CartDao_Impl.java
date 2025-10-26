package cl.duoc.guaumiau.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.duoc.guaumiau.data.model.CartItem;
import cl.duoc.guaumiau.data.model.CartItemWithProduct;
import cl.duoc.guaumiau.data.model.PetType;
import cl.duoc.guaumiau.data.model.ProductCategory;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CartDao_Impl implements CartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CartItem> __insertionAdapterOfCartItem;

  private final EntityDeletionOrUpdateAdapter<CartItem> __deletionAdapterOfCartItem;

  private final EntityDeletionOrUpdateAdapter<CartItem> __updateAdapterOfCartItem;

  private final SharedSQLiteStatement __preparedStmtOfClearCart;

  private final Converters __converters = new Converters();

  public CartDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCartItem = new EntityInsertionAdapter<CartItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cart_items` (`id`,`userId`,`productId`,`quantity`,`addedAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CartItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getProductId());
        statement.bindLong(4, entity.getQuantity());
        statement.bindLong(5, entity.getAddedAt());
      }
    };
    this.__deletionAdapterOfCartItem = new EntityDeletionOrUpdateAdapter<CartItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cart_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CartItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCartItem = new EntityDeletionOrUpdateAdapter<CartItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cart_items` SET `id` = ?,`userId` = ?,`productId` = ?,`quantity` = ?,`addedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CartItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getProductId());
        statement.bindLong(4, entity.getQuantity());
        statement.bindLong(5, entity.getAddedAt());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfClearCart = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cart_items WHERE userId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCartItem(final CartItem cartItem,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCartItem.insertAndReturnId(cartItem);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCartItem(final CartItem cartItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCartItem.handle(cartItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCartItem(final CartItem cartItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCartItem.handle(cartItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearCart(final int userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearCart.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearCart.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CartItemWithProduct>> getCartItemsWithProducts(final int userId) {
    final String _sql = "\n"
            + "        SELECT cart_items.id,\n"
            + "               cart_items.userId,\n"
            + "               cart_items.productId,\n"
            + "               cart_items.quantity,\n"
            + "               cart_items.addedAt,\n"
            + "               products.name as productName,\n"
            + "               products.description as productDescription,\n"
            + "               products.price as productPrice,\n"
            + "               products.category as productCategory,\n"
            + "               products.petType as productPetType,\n"
            + "               products.imageUrl as productImageUrl,\n"
            + "               products.isSustainable as productIsSustainable,\n"
            + "               products.isInnovative as productIsInnovative,\n"
            + "               products.stock as productStock,\n"
            + "               products.rating as productRating\n"
            + "        FROM cart_items \n"
            + "        INNER JOIN products ON cart_items.productId = products.id \n"
            + "        WHERE cart_items.userId = ?\n"
            + "        ORDER BY cart_items.addedAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cart_items",
        "products"}, new Callable<List<CartItemWithProduct>>() {
      @Override
      @NonNull
      public List<CartItemWithProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfUserId = 1;
          final int _cursorIndexOfProductId = 2;
          final int _cursorIndexOfQuantity = 3;
          final int _cursorIndexOfAddedAt = 4;
          final int _cursorIndexOfProductName = 5;
          final int _cursorIndexOfProductDescription = 6;
          final int _cursorIndexOfProductPrice = 7;
          final int _cursorIndexOfProductCategory = 8;
          final int _cursorIndexOfProductPetType = 9;
          final int _cursorIndexOfProductImageUrl = 10;
          final int _cursorIndexOfProductIsSustainable = 11;
          final int _cursorIndexOfProductIsInnovative = 12;
          final int _cursorIndexOfProductStock = 13;
          final int _cursorIndexOfProductRating = 14;
          final List<CartItemWithProduct> _result = new ArrayList<CartItemWithProduct>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CartItemWithProduct _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpProductId;
            _tmpProductId = _cursor.getInt(_cursorIndexOfProductId);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpProductDescription;
            _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
            final double _tmpProductPrice;
            _tmpProductPrice = _cursor.getDouble(_cursorIndexOfProductPrice);
            final ProductCategory _tmpProductCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfProductCategory);
            _tmpProductCategory = __converters.toProductCategory(_tmp);
            final PetType _tmpProductPetType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfProductPetType);
            _tmpProductPetType = __converters.toPetType(_tmp_1);
            final String _tmpProductImageUrl;
            _tmpProductImageUrl = _cursor.getString(_cursorIndexOfProductImageUrl);
            final boolean _tmpProductIsSustainable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfProductIsSustainable);
            _tmpProductIsSustainable = _tmp_2 != 0;
            final boolean _tmpProductIsInnovative;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfProductIsInnovative);
            _tmpProductIsInnovative = _tmp_3 != 0;
            final int _tmpProductStock;
            _tmpProductStock = _cursor.getInt(_cursorIndexOfProductStock);
            final float _tmpProductRating;
            _tmpProductRating = _cursor.getFloat(_cursorIndexOfProductRating);
            _item = new CartItemWithProduct(_tmpId,_tmpUserId,_tmpProductId,_tmpQuantity,_tmpAddedAt,_tmpProductName,_tmpProductDescription,_tmpProductPrice,_tmpProductCategory,_tmpProductPetType,_tmpProductImageUrl,_tmpProductIsSustainable,_tmpProductIsInnovative,_tmpProductStock,_tmpProductRating);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCartItem(final int userId, final int productId,
      final Continuation<? super CartItem> $completion) {
    final String _sql = "SELECT * FROM cart_items WHERE userId = ? AND productId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, productId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CartItem>() {
      @Override
      @Nullable
      public CartItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final CartItem _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpProductId;
            _tmpProductId = _cursor.getInt(_cursorIndexOfProductId);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _result = new CartItem(_tmpId,_tmpUserId,_tmpProductId,_tmpQuantity,_tmpAddedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getCartItemCount(final int userId) {
    final String _sql = "SELECT COUNT(*) FROM cart_items WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cart_items"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getTotalQuantity(final int userId) {
    final String _sql = "SELECT SUM(quantity) FROM cart_items WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cart_items"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
