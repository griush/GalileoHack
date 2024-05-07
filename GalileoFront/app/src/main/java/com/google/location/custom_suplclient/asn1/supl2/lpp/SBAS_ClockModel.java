// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.asn1.supl2.lpp;

// Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//
//
import com.google.location.custom_suplclient.asn1.base.Asn1Integer;
import com.google.location.custom_suplclient.asn1.base.Asn1Object;
import com.google.location.custom_suplclient.asn1.base.Asn1Sequence;
import com.google.location.custom_suplclient.asn1.base.Asn1Tag;
import com.google.location.custom_suplclient.asn1.base.BitStream;
import com.google.location.custom_suplclient.asn1.base.BitStreamReader;
import com.google.location.custom_suplclient.asn1.base.SequenceComponent;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
* 
*/
public  class SBAS_ClockModel extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_SBAS_ClockModel
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public SBAS_ClockModel() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_SBAS_ClockModel;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_SBAS_ClockModel != null) {
      return ImmutableList.of(TAG_SBAS_ClockModel);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new SBAS_ClockModel from encoded stream.
   */
  public static SBAS_ClockModel fromPerUnaligned(byte[] encodedBytes) {
    SBAS_ClockModel result = new SBAS_ClockModel();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new SBAS_ClockModel from encoded stream.
   */
  public static SBAS_ClockModel fromPerAligned(byte[] encodedBytes) {
    SBAS_ClockModel result = new SBAS_ClockModel();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }



  @Override protected boolean isExtensible() {
    return true;
  }

  @Override public boolean containsExtensionValues() {
    for (SequenceComponent extensionComponent : getExtensionComponents()) {
      if (extensionComponent.isExplicitlySet()) return true;
    }
    return false;
  }

  
  private SBAS_ClockModel.sbasToType sbasTo_;
  public SBAS_ClockModel.sbasToType getSbasTo() {
    return sbasTo_;
  }
  /**
   * @throws ClassCastException if value is not a SBAS_ClockModel.sbasToType
   */
  public void setSbasTo(Asn1Object value) {
    this.sbasTo_ = (SBAS_ClockModel.sbasToType) value;
  }
  public SBAS_ClockModel.sbasToType setSbasToToNewInstance() {
    sbasTo_ = new SBAS_ClockModel.sbasToType();
    return sbasTo_;
  }
  
  private SBAS_ClockModel.sbasAgfoType sbasAgfo_;
  public SBAS_ClockModel.sbasAgfoType getSbasAgfo() {
    return sbasAgfo_;
  }
  /**
   * @throws ClassCastException if value is not a SBAS_ClockModel.sbasAgfoType
   */
  public void setSbasAgfo(Asn1Object value) {
    this.sbasAgfo_ = (SBAS_ClockModel.sbasAgfoType) value;
  }
  public SBAS_ClockModel.sbasAgfoType setSbasAgfoToNewInstance() {
    sbasAgfo_ = new SBAS_ClockModel.sbasAgfoType();
    return sbasAgfo_;
  }
  
  private SBAS_ClockModel.sbasAgf1Type sbasAgf1_;
  public SBAS_ClockModel.sbasAgf1Type getSbasAgf1() {
    return sbasAgf1_;
  }
  /**
   * @throws ClassCastException if value is not a SBAS_ClockModel.sbasAgf1Type
   */
  public void setSbasAgf1(Asn1Object value) {
    this.sbasAgf1_ = (SBAS_ClockModel.sbasAgf1Type) value;
  }
  public SBAS_ClockModel.sbasAgf1Type setSbasAgf1ToNewInstance() {
    sbasAgf1_ = new SBAS_ClockModel.sbasAgf1Type();
    return sbasAgf1_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getSbasTo() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getSbasTo();
          }

          @Override public void setToNewInstance() {
            setSbasToToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? SBAS_ClockModel.sbasToType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "sbasTo : "
                    + getSbasTo().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 1);

          @Override public boolean isExplicitlySet() {
            return getSbasAgfo() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getSbasAgfo();
          }

          @Override public void setToNewInstance() {
            setSbasAgfoToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? SBAS_ClockModel.sbasAgfoType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "sbasAgfo : "
                    + getSbasAgfo().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 2);

          @Override public boolean isExplicitlySet() {
            return getSbasAgf1() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getSbasAgf1();
          }

          @Override public void setToNewInstance() {
            setSbasAgf1ToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? SBAS_ClockModel.sbasAgf1Type.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "sbasAgf1 : "
                    + getSbasAgf1().toIndentedString(indent);
              }
        });
    
    return builder.build();
  }

  @Override public Iterable<? extends SequenceComponent>
                                                    getExtensionComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
      
      return builder.build();
    }

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class sbasToType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_sbasToType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public sbasToType() {
    super();
    setValueRange(new java.math.BigInteger("0"), new java.math.BigInteger("5399"));

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_sbasToType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_sbasToType != null) {
      return ImmutableList.of(TAG_sbasToType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new sbasToType from encoded stream.
   */
  public static sbasToType fromPerUnaligned(byte[] encodedBytes) {
    sbasToType result = new sbasToType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new sbasToType from encoded stream.
   */
  public static sbasToType fromPerAligned(byte[] encodedBytes) {
    sbasToType result = new sbasToType();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "sbasToType = " + getInteger() + ";\n";
  }
}

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class sbasAgfoType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_sbasAgfoType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public sbasAgfoType() {
    super();
    setValueRange(new java.math.BigInteger("-2048"), new java.math.BigInteger("2047"));

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_sbasAgfoType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_sbasAgfoType != null) {
      return ImmutableList.of(TAG_sbasAgfoType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new sbasAgfoType from encoded stream.
   */
  public static sbasAgfoType fromPerUnaligned(byte[] encodedBytes) {
    sbasAgfoType result = new sbasAgfoType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new sbasAgfoType from encoded stream.
   */
  public static sbasAgfoType fromPerAligned(byte[] encodedBytes) {
    sbasAgfoType result = new sbasAgfoType();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "sbasAgfoType = " + getInteger() + ";\n";
  }
}

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class sbasAgf1Type extends Asn1Integer {
  //

  private static final Asn1Tag TAG_sbasAgf1Type
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public sbasAgf1Type() {
    super();
    setValueRange(new java.math.BigInteger("-128"), new java.math.BigInteger("127"));

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_sbasAgf1Type;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_sbasAgf1Type != null) {
      return ImmutableList.of(TAG_sbasAgf1Type);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new sbasAgf1Type from encoded stream.
   */
  public static sbasAgf1Type fromPerUnaligned(byte[] encodedBytes) {
    sbasAgf1Type result = new sbasAgf1Type();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new sbasAgf1Type from encoded stream.
   */
  public static sbasAgf1Type fromPerAligned(byte[] encodedBytes) {
    sbasAgf1Type result = new sbasAgf1Type();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "sbasAgf1Type = " + getInteger() + ";\n";
  }
}

  

    

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    StringBuilder builder = new StringBuilder();
    builder.append("SBAS_ClockModel = {\n");
    final String internalIndent = indent + "  ";
    for (SequenceComponent component : getComponents()) {
      if (component.isExplicitlySet()) {
        builder.append(internalIndent)
            .append(component.toIndentedString(internalIndent));
      }
    }
    if (isExtensible()) {
      builder.append(internalIndent).append("...\n");
      for (SequenceComponent component : getExtensionComponents()) {
        if (component.isExplicitlySet()) {
          builder.append(internalIndent)
              .append(component.toIndentedString(internalIndent));
        }
      }
    }
    builder.append(indent).append("};\n");
    return builder.toString();
  }
}
